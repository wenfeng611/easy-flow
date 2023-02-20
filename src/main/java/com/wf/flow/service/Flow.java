package com.wf.flow.service;

import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.handler.context.FlowContext;
import com.wf.flow.mapper.FlowItemDetailMapper;
import com.wf.flow.mapper.FlowItemMapper;
import com.wf.flow.mapper.FlowConfigMapper;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import com.wf.flow.service.strage.IFlowHandleStrategy;
import com.wf.flow.utils.SpringContextUtils;
import com.wf.flow.utils.graph.GraphResolvor;
import org.javatuples.Pair;

import java.util.Date;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description  流程
 * @date 2020/9/16 17:23
 */


public class Flow {

    //获取字段的策略
    private IFlowHandleStrategy flowHandleStrategy;

    //场景code
    private String sceneCode;

    private String username;

    public Flow(String username, String sceneCode, IFlowHandleStrategy flowHandleStrategy){
        this.flowHandleStrategy = flowHandleStrategy;
        this.sceneCode = sceneCode;
        this.username = username;
    }

    public static Flow initFlow(String username, String sceneCode, IFlowHandleStrategy flowHandleStrategy){
        return new Flow(username,sceneCode,flowHandleStrategy);
    }

    /**
     *  开始一个流程 flowHandleStrategy中的字段值通过businessId查出来或者计算出来需要在回调中保存住流程返回的业务的id 保证条件节点查找字段的时候不会出错
     * @return
     */
    public FlowResult runFlow(Object businessData){
        FlowItemMapper flowItemMapper = SpringContextUtils.getBean(FlowItemMapper.class);

        FlowContext context = new FlowContext(getFlowHandleStrategy(),getSceneCode(),getUsername());
        //新建流程
        FlowItemEntity flowItemEntity = initFlowItem(username,sceneCode);
        //保存 flowItemEntity  其id就是业务需要保存的id
        flowItemMapper.save(flowItemEntity);
        //回调业务 保存 业务id
        getFlowHandleStrategy().callSaveBusinessId(flowItemEntity.getId(),businessData);
        context.setBusinessId(flowItemEntity.getId());
        context.setBusinessData(businessData);

        FlowResult flowResult = context.run();
        //回调中如果需要用到flow 就是不用等待人工直接继续流程 把usernane改成系统
        setUsername("系统");
        getFlowHandleStrategy().callAfterRun(flowResult);
        return flowResult;
    }

    /**
     *  继续流程
     * @param businessId  createFlow时返回的businessId  需要业务记录下来
     * @param operateInfo 操作内容
     * @param terminalFlow 是否需要终止流程 默认继续往下走  如果遇到人工驳回 需要终止流程
     * @return
     */
    public FlowResult continueFlow(Integer businessId,String operateInfo,boolean terminalFlow){
        return continueFlow(businessId,operateInfo,terminalFlow,null);
    }

    /**
     *
     * @param businessId  createFlow时返回的businessId  需要业务记录下来
     * @param operateInfo 操作结果
     * @param terminalFlow 是否需要终止流程 默认继续往下走  如果遇到人工驳回 需要终止流程
     * @param businessData  业务需要传递到回调中的参数
     * @return
     */
    public FlowResult continueFlow(Integer businessId,String operateInfo,boolean terminalFlow,Object businessData){
        //根据businessId找到flowitem  businessId即flowitem的id
        FlowItemMapper flowItemMapper = SpringContextUtils.getBean(FlowItemMapper.class);
        FlowConfigMapper flowMapper = SpringContextUtils.getBean(FlowConfigMapper.class);
        FlowItemDetailMapper detailMapper = SpringContextUtils.getBean(FlowItemDetailMapper.class);

        FlowItemEntity flowItem = flowItemMapper.findById(businessId);
        if(Objects.isNull(flowItem)) {
            return FlowResult.fail("查询不到这个流程信息",businessId, FlowEnums.FlowItemStatusEnums.NOTFOUND.getCode());
        }

        if(FlowEnums.FlowItemStatusEnums.END.getCode() == flowItem.getFlowStatus() || FlowEnums.FlowItemStatusEnums.TERMINAL.getCode() == flowItem.getFlowStatus()){
            return FlowResult.fail("该流程状态是: "+FlowEnums.FlowItemStatusEnums.from(flowItem.getFlowStatus()),businessId,flowItem.getFlowStatus());
        }

        FlowContext context = null;
        FlowConfigEntity flow = flowMapper.findTopById(flowItem.getFlowId());

        GNode currentNode = GraphResolvor.getNodeByG6NodeId(flow.getGraphJson(), flowItem.getCurrentNodeId());

        if(terminalFlow){
            //如果需要终止流程
            context = new FlowContext(getFlowHandleStrategy(),getSceneCode(),new GNode(FlowEnums.FlowNodeTypeEnums.TERMINAL.getCode()),businessId,getUsername());
            FlowItemDetailEntity detail = FlowItemDetailEntity.buildDefault(currentNode, null, businessId, operateInfo);
            detail.setOperateUsername(getUsername());
            detailMapper.save(detail);
        }else {
            //找到下一个节点 第一个 设置到流程Context中
            GNode next = currentNode;
            //等待节点直接找下一个节点
            if(FlowEnums.FlowNodeTypeEnums.WAITING.getCode().equals(currentNode.getNodeType())){
                next = GraphResolvor.getFirstNextNodeBYG6NodeId(flow.getGraphJson(), flowItem.getCurrentNodeId());
            }

            context = new FlowContext(getFlowHandleStrategy(), getSceneCode(), next, businessId, flow, getUsername());
            FlowItemDetailEntity detail = FlowItemDetailEntity.buildDefault(currentNode, next, businessId, operateInfo);
            detail.setOperateUsername(getUsername());
            detailMapper.save(detail);
        }
        context.setBusinessData(businessData);
        FlowResult flowResult = context.run();
        //回调中如果需要用到flow 就是不用等待人工直接继续流程 把usernane改成系统
        setUsername("系统");
        getFlowHandleStrategy().callAfterContinue(flowResult,businessData);
        return flowResult;
    }


    /**
     *  第一次没找到流程 重启流程的时候调用
     *
     * @param businessId createFlow时返回的businessId  需要业务记录下来
     *
     */
    public Pair<Boolean,String> restartFlow(Integer businessId){
        FlowItemMapper flowItemMapper = SpringContextUtils.getBean(FlowItemMapper.class);
        FlowItemDetailMapper detailMapper = SpringContextUtils.getBean(FlowItemDetailMapper.class);
        FlowItemEntity flowItemEntity = flowItemMapper.findById(businessId);
        if(flowItemEntity.getFlowId() != 0){
            return Pair.with(false,"当前状态不能重启");
        }
        FlowContext context = new FlowContext(getFlowHandleStrategy(),getSceneCode(),getUsername());
        context.setBusinessId(businessId);
        //重启流程记录
        FlowItemDetailEntity detail = FlowItemDetailEntity.buildDefault(null, null, businessId, "重启流程");
        detail.setOperateUsername(getUsername());
        detailMapper.save(detail);

        //回调业务
        getFlowHandleStrategy().callAfterRestart(context.run());
        return Pair.with(true,null);
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
