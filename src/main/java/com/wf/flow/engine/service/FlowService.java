package com.wf.flow.engine.service;

import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.cache.service.CacheService;
import com.wf.flow.engine.graph.GraphResolverService;
import com.wf.flow.engine.strategy.IFlowHandleStrategy;
import com.wf.flow.utils.NodeConfigParseTool;
import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.entity.FlowRuleEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.mapper.FlowItemDetailMapper;
import com.wf.flow.mapper.FlowItemMapper;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import com.wf.flow.utils.JSONUtil;
import com.wf.flow.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author wenfeng.zhu
 * @description  流程
 * @date 2020/9/16 17:23
 */


public class FlowService {

    //获取字段的策略
    private IFlowHandleStrategy flowHandleStrategy;

    //场景code
    private String sceneCode;

    private String username;

    public FlowService(String username, String sceneCode, IFlowHandleStrategy flowHandleStrategy){
        this.flowHandleStrategy = flowHandleStrategy;
        this.sceneCode = sceneCode;
        this.username = username;
    }

    public static FlowService initFlow(String username, String sceneCode, IFlowHandleStrategy flowHandleStrategy){
        return new FlowService(username,sceneCode,flowHandleStrategy);
    }

    /**
     *  开始一个流程
     * @return
     */
    public FlowResult runFlow(String customId, Object businessData, Map<String, Object> params){
        FlowItemMapper flowItemMapper = SpringContextUtils.getBean(FlowItemMapper.class);

        FlowContext context = new FlowContext(getFlowHandleStrategy(),getSceneCode(),getUsername());
        //新建流程
        FlowItemEntity flowItemEntity = initFlowItem(username,sceneCode);
        flowItemEntity.setBusinessCustomId(customId);
        //保存 flowItemEntity  其id就是业务需要保存的id
        flowItemMapper.save(flowItemEntity);
        //回调业务 保存 业务id
        getFlowHandleStrategy().callSaveBusinessId(flowItemEntity.getId(),businessData);
        context.setBusinessId(flowItemEntity.getId());
        context.setBusinessData(businessData);
        context.setParams(Objects.nonNull(params)?params: new HashMap());
        context.setCustomId(customId);
        context.setMainSceneCode(getSceneCode());

        FlowResult flowResult = context.run();
        //回调中如果需要用到flow 就是不用等待人工直接继续流程 把usernane改成系统
        setUsername("System");

        flowResult.setBusinessId(customId);
        return flowResult;
    }

    /**
     *
     * @param customId  createFlow时传的customId
     * @param operateInfo 操作结果
     * @param terminalFlow 是否需要终止流程 默认继续往下走  如果遇到人工驳回 需要终止流程
     * @param eventName 触发指定事件的等待节点
     * @param businessData  业务需要传递到回调中的参数
     * @return
     */
    public FlowResult continueFlow(String customId, String operateInfo, boolean terminalFlow, String eventName, Object businessData, Map<String, Object> params){
        //根据businessId找到flowitem  businessId即flowitem的id
        FlowItemMapper flowItemMapper = SpringContextUtils.getBean(FlowItemMapper.class);
        FlowItemDetailMapper detailMapper = SpringContextUtils.getBean(FlowItemDetailMapper.class);
        GraphResolverService graphResolver = SpringContextUtils.getBean(GraphResolverService.class);
        CacheService cacheService = SpringContextUtils.getBean(CacheService.class);

        FlowItemEntity flowItem = flowItemMapper.findByCustomId(customId);
        if(Objects.isNull(flowItem)) {
            return FlowResult.fail("查询不到这个流程信息",customId);
        }

        if(FlowEnums.FlowItemStatusEnums.END.getCode() == flowItem.getFlowStatus() || FlowEnums.FlowItemStatusEnums.TERMINAL.getCode() == flowItem.getFlowStatus()){
            return FlowResult.fail("该流程状态是: "+ FlowEnums.FlowItemStatusEnums.from(flowItem.getFlowStatus()),customId);
        }

        FlowContext context = null;
        FlowConfigEntity flow = cacheService.getFlowConfig(flowItem.getSceneCode());

        GNode currentNode = graphResolver.getNodeByG6NodeId(flow.getGraphJson(), flowItem.getCurrentNodeId());
        Integer businessId = flowItem.getId();
        String currentScene = flowItem.getSceneCode();
        if(terminalFlow){
            //如果需要终止流程
            context = new FlowContext(getFlowHandleStrategy(),currentScene,new GNode(FlowEnums.FlowNodeTypeEnums.TERMINAL.getCode()),businessId,getUsername());
            FlowItemDetailEntity detail = FlowItemDetailEntity.buildDefault(currentNode, null, businessId, operateInfo);
            detail.setOperateUsername(getUsername());
            detailMapper.save(detail);

            FlowResult flowResult = context.run();
            //回调中如果需要用到flow 就是不用等待人工直接继续流程 把usernane改成系统
            setUsername("系统");
            flowResult.setBusinessId(customId);
            return flowResult;
        }

        //找到下一个节点 第一个 设置到流程Context中
        GNode next = currentNode;
        context = new FlowContext(getFlowHandleStrategy(), currentScene, next, businessId, flow, getUsername());
        context.setBusinessData(businessData);
        context.setCustomId(customId);
        context.setMainSceneCode(getSceneCode());
        //设置参数
        Map<String, Object> paramMap = new HashMap();
        //持久化的参数加入到context中
        String paramJson = flowItem.getParamsJson();
        if(StringUtils.isNotBlank(paramJson)){
            Map<String, Object> map = JSONUtil.jsonToObj(paramJson, HashMap.class);
            paramMap.putAll(map);
        }
        if(Objects.nonNull(params)){
            Map<String, Object> uniqueKeyMap = new HashMap();
            //参数加上节点的唯一g6id 保证参数具体到节点 防止参数重复覆盖
            params.forEach((key, value) -> uniqueKeyMap.put(currentNode.getId() + "-" + key, value));
            paramMap.putAll(uniqueKeyMap);
        }
        context.setParams(paramMap);

        //判断是否有指定event
        if(StringUtils.isNotBlank(eventName)){
            //如果不是等待节点 触发错误 跳过
            if(!FlowEnums.FlowNodeTypeEnums.WAITING.getCode().equals(currentNode.getNodeType())){
                return FlowResult.fail("触发事件"+eventName+" 当前节点不是等待触发 不继续执行流程",customId);
            }
            //查找节点规则 判断是否符合节点对应的result
            FlowRuleEntity waitNodeRule = cacheService.getFlowNodeRule(flow.getId(), currentNode.getId());
            if(Objects.isNull(waitNodeRule)) {
                return FlowResult.fail("触发事件"+eventName+" 当前节点未配置规则 不继续执行流程",customId);
            }
            Map nodeEntity = NodeConfigParseTool.getNodeConfigEntity(waitNodeRule.getAutoJudgeRule(), Map.class);
            String event = (String) nodeEntity.get("event");
            if(!Arrays.asList(event.split(",")).contains(eventName)){
                return FlowResult.fail("事件"+eventName+"不匹配 "+event+" 不继续执行流程",customId);
            }
        }

        //等待节点直接找下一个节点
//        if(FlowEnums.FlowNodeTypeEnums.WAITING.getCode().equals(currentNode.getNodeType())){
            next = graphResolver.getFirstNextNodeBYG6NodeId(context, flowItem.getCurrentNodeId());
            context.setCurrentNode(next);
//        }

        FlowItemDetailEntity detail = FlowItemDetailEntity.buildDefault(currentNode, next, businessId, operateInfo);
        detail.setOperateUsername(getUsername());
        detailMapper.save(detail);

        FlowResult flowResult = context.run();
        //回调中如果需要用到flow 就是不用等待人工直接继续流程 把usernane改成系统
        setUsername("系统");

        flowResult.setBusinessId(customId);
        return flowResult;
    }

    /**
     *  继续流程
     * @param customId
     * @param operateInfo 操作内容
     * @param terminalFlow 是否需要终止流程 默认继续往下走  如果遇到人工驳回 需要终止流程
     * @return
     */
    public FlowResult continueFlow(String customId, String operateInfo, boolean terminalFlow){
        return continueFlow(customId,operateInfo,terminalFlow,null,null,null);
    }

    public FlowResult continueFlow(String customId, String operateInfo, boolean terminalFlow, Object businessData, Map<String, Object> params){
        return continueFlow(customId,operateInfo,terminalFlow,null,businessData,params);
    }

    private FlowItemEntity initFlowItem(String username, String sceneCode) {
        return FlowItemEntity.builder()
                .flowId(0)
                .currentNodeId("")
                .sceneCode(sceneCode)
                .flowStatus(FlowEnums.FlowItemStatusEnums.INIT.getCode())
                .createrName(username)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
    }

    public String getSceneCode() {
        return sceneCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public IFlowHandleStrategy getFlowHandleStrategy() {
        return flowHandleStrategy;
    }
}
