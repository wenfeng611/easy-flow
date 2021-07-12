package com.auditflow.customize.service.auditstream;

import com.auditflow.customize.entity.AuditFlowEntity;
import com.auditflow.customize.entity.AuditFlowItemDetailEntity;
import com.auditflow.customize.entity.AuditFlowItemEntity;
import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.handler.auditstream.context.FlowContext;
import com.auditflow.customize.mapper.AuditFlowItemDetailMapper;
import com.auditflow.customize.mapper.AuditFlowItemMapper;
import com.auditflow.customize.mapper.AuditFlowMapper;
import com.auditflow.customize.model.auditstream.FlowResult;
import com.auditflow.customize.model.auditstream.GNode;
import com.auditflow.customize.service.auditstream.strage.IFlowHandleStrategy;
import com.auditflow.customize.utils.SpringContextUtils;
import com.auditflow.customize.utils.graph.GraphResolvor;
import org.javatuples.Pair;

import java.util.Date;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description  审核流程
 * @date 2020/9/16 17:23
 */


public class AuditFlow {

    //获取字段的策略
    private IFlowHandleStrategy flowHandleStrategy;

    //场景code
    private String sceneCode;

    private String username;

    public AuditFlow(String username, String sceneCode, IFlowHandleStrategy flowHandleStrategy){
        this.flowHandleStrategy = flowHandleStrategy;
        this.sceneCode = sceneCode;
        this.username = username;
    }

    public static  AuditFlow initFlow(String username, String sceneCode, IFlowHandleStrategy flowHandleStrategy){
        return new AuditFlow(username,sceneCode,flowHandleStrategy);
    }

    /**
     *  开始一个流程 如果flowHandleStrategy里面不需要通过businessId计算字段的值可以调用这个方法，然后把返回值里面的业务id存下来
     * @return
     */
    public FlowResult runFlow(){
        FlowContext context = new FlowContext(getFlowHandleStrategy(),getSceneCode(),getUsername());
        FlowResult flowResult = context.run();
        //回调中如果需要用到flow 就是不用等待人工直接继续流程 把usernane改成系统
        setUsername("系统");
        getFlowHandleStrategy().callAfterRun(flowResult);
        return flowResult;
    }

    /**
     *  开始一个流程 flowHandleStrategy中的字段值通过businessId查出来或者计算出来需要在回调中保存住流程返回的业务的id 保证条件节点查找字段的时候不会出错
     * @return
     */
    public FlowResult runFlow(Object businessData){
        AuditFlowItemMapper flowItemMapper = SpringContextUtils.getBean(AuditFlowItemMapper.class);

        FlowContext context = new FlowContext(getFlowHandleStrategy(),getSceneCode(),getUsername());
        //新建流程
        AuditFlowItemEntity auditFlowItemEntity = initFlowItem(username,sceneCode);
        //保存 auditFlowItemEntity  其id就是业务需要保存的id
        flowItemMapper.save(auditFlowItemEntity);
        //回调业务 保存 业务id
        getFlowHandleStrategy().callSaveBusinessId(auditFlowItemEntity.getId(),businessData);
        context.setBusinessId(auditFlowItemEntity.getId());

        FlowResult flowResult = context.run();
        //回调中如果需要用到flow 就是不用等待人工直接继续流程 把usernane改成系统
        setUsername("系统");
        getFlowHandleStrategy().callAfterRun(flowResult);
        return flowResult;
    }

    /**
     *  继续流程
     * @param businessId  createFlow时返回的businessId  需要业务记录下来
     * @param auditResult 审核结果
     * @param terminalFlow 是否需要终止流程 默认继续往下走  如果遇到人工驳回 需要终止流程
     * @return
     */
    public FlowResult continueFlow(Integer businessId,String auditResult,boolean terminalFlow){
        return continueFlow(businessId,auditResult,terminalFlow,null);
    }

    /**
     *
     * @param businessId  createFlow时返回的businessId  需要业务记录下来
     * @param auditResult 审核结果
     * @param terminalFlow 是否需要终止流程 默认继续往下走  如果遇到人工驳回 需要终止流程
     * @param callbackBusinessData  业务需要传递到回调中的参数
     * @return
     */
    public FlowResult continueFlow(Integer businessId,String auditResult,boolean terminalFlow,Object callbackBusinessData){
        //根据businessId找到flowitem  businessId即flowitem的id
        AuditFlowItemMapper flowItemMapper = SpringContextUtils.getBean(AuditFlowItemMapper.class);
        AuditFlowMapper flowMapper = SpringContextUtils.getBean(AuditFlowMapper.class);
        AuditFlowItemDetailMapper detailMapper = SpringContextUtils.getBean(AuditFlowItemDetailMapper.class);

        AuditFlowItemEntity flowItem = flowItemMapper.findById(businessId);
        if(Objects.isNull(flowItem)) {
            return FlowResult.fail("查询不到这个流程信息",businessId, AuditStreamEnums.FlowAuditStatusEnums.NOTFOUND.getCode());
        }

        if(flowItem.getAuditStatus() > AuditStreamEnums.FlowAuditStatusEnums.RUNNING.getCode()){
            return FlowResult.fail("该流程状态是: "+AuditStreamEnums.FlowAuditStatusEnums.from(flowItem.getAuditStatus()),businessId,flowItem.getAuditStatus());
        }

        FlowContext context = null;
        AuditFlowEntity flow = flowMapper.findTopById(flowItem.getFlowId());

        GNode currentNode = GraphResolvor.getNodeByG6NodeId(flow.getGraphJson(), flowItem.getCurrentNodeId());

        if(terminalFlow){
            //如果需要终止流程
            context = new FlowContext(getFlowHandleStrategy(),getSceneCode(),new GNode(AuditStreamEnums.FlowNodeTypeEnums.TERMINAL.getCode()),businessId,getUsername());
            AuditFlowItemDetailEntity detail = AuditFlowItemDetailEntity.buildDefault(currentNode, null, businessId, AuditStreamEnums.FlowAuditStatusEnums.RUNNING.getCode(), auditResult);
            detail.setAuditUsername(getUsername());
            detailMapper.save(detail);
        }else {
            //找到下一个节点 第一个 设置到流程Context中
            GNode next = GraphResolvor.getFirstNextNodeBYG6NodeId(flow.getGraphJson(), flowItem.getCurrentNodeId());
            context = new FlowContext(getFlowHandleStrategy(), getSceneCode(), next, businessId, flow, getUsername());
            AuditFlowItemDetailEntity detail = AuditFlowItemDetailEntity.buildDefault(currentNode, next, businessId, AuditStreamEnums.FlowAuditStatusEnums.RUNNING.getCode(), auditResult);
            detail.setAuditUsername(getUsername());
            detailMapper.save(detail);
        }
        FlowResult flowResult = context.run();
        //回调中如果需要用到flow 就是不用等待人工直接继续流程 把usernane改成系统
        setUsername("系统");
        getFlowHandleStrategy().callAfterContinue(flowResult,callbackBusinessData);
        return flowResult;
    }


    /**
     *  第一次没找到流程 重启流程的时候调用
     *
     * @param businessId createFlow时返回的businessId  需要业务记录下来
     *
     */
    public Pair<Boolean,String> restartFlow(Integer businessId){
        AuditFlowItemMapper flowItemMapper = SpringContextUtils.getBean(AuditFlowItemMapper.class);
        AuditFlowItemDetailMapper detailMapper = SpringContextUtils.getBean(AuditFlowItemDetailMapper.class);
        AuditFlowItemEntity flowItemEntity = flowItemMapper.findById(businessId);
        if(flowItemEntity.getFlowId() != 0){
            return Pair.with(false,"当前状态不能重启");
        }
        FlowContext context = new FlowContext(getFlowHandleStrategy(),getSceneCode(),getUsername());
        context.setBusinessId(businessId);
        //重启流程记录
        AuditFlowItemDetailEntity detail = AuditFlowItemDetailEntity.buildDefault(null, null, businessId, AuditStreamEnums.FlowAuditStatusEnums.NOTFOUND.getCode(), "重启流程");
        detail.setAuditUsername(getUsername());
        detailMapper.save(detail);

        //回调业务
        getFlowHandleStrategy().callAfterRestart(context.run());
        return Pair.with(true,null);
    }

    private AuditFlowItemEntity initFlowItem(String username, String sceneCode) {
        return AuditFlowItemEntity.builder()
                .flowId(0)
                .currentNodeId("")
                .auditStatus(AuditStreamEnums.FlowAuditStatusEnums.WAITING.getCode())
                .sceneCode(sceneCode)
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
