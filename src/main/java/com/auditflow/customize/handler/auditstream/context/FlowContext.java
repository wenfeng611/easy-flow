package com.auditflow.customize.handler.auditstream.context;

import com.auditflow.customize.entity.AuditFlowEntity;
import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.handler.auditstream.*;
import com.auditflow.customize.model.auditstream.FlowResult;
import com.auditflow.customize.model.auditstream.GNode;
import com.auditflow.customize.service.auditstream.strage.IFlowHandleStrategy;
import com.auditflow.customize.utils.SpringContextUtils;

/**
 * @author wenfeng.zhu
 * @description   流程状态记录状态对象
 */
public class FlowContext {

    private State state;

    private String sceneCode;
    private IFlowHandleStrategy fieldStrategy;

    private GNode currentNode;
    private AuditFlowEntity flow;
    private Integer businessId;

    private String username;

    //默认创建Condition处理状态
    public FlowContext(IFlowHandleStrategy fieldStrategy, String sceneCode, String username) {
        this.fieldStrategy = fieldStrategy;
        this.sceneCode = sceneCode;
        this.username = username;
        this.state=  SpringContextUtils.getBean(ConditionNodeHandler.class);
    }

    public FlowContext(IFlowHandleStrategy fieldStrategy, String sceneCode, GNode node, Integer businessId, String username) {
        this.fieldStrategy = fieldStrategy;
        this.sceneCode = sceneCode;
        this.currentNode= node;
        this.businessId=businessId;
        this.username=username;
        AuditStreamEnums.FlowNodeTypeEnums flowNodeType = AuditStreamEnums.FlowNodeTypeEnums.get(node.getNodeType());
        switch (flowNodeType){
            case CUSTOMER:
                this.state= SpringContextUtils.getBean(CustomNodeHandler.class);
                break;
            case ABORD:
                this.state=SpringContextUtils.getBean(AbortNodeHandler.class);
                break;
            case END:
                this.state=SpringContextUtils.getBean(EndNodeHandler.class);
                break;
            case TERMINAL:
                this.state =SpringContextUtils.getBean(TerminalNodeHandler.class);
        }
    }

    public FlowContext(IFlowHandleStrategy fieldStrategy, String sceneCode, GNode node, Integer businessId, AuditFlowEntity flow, String username) {
        this(fieldStrategy,sceneCode,node,businessId,username);
        this.flow=flow;
    }

    public FlowResult run(){
        return state.handle(this);
    }

    public GNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(GNode currentNode) {
        this.currentNode = currentNode;
    }

    public AuditFlowEntity getFlow() {
        return flow;
    }

    public void setFlow(AuditFlowEntity flow) {
        this.flow = flow;
    }

    public IFlowHandleStrategy getFieldStrategy() {
        return fieldStrategy;
    }

    public void setFieldStrategy(IFlowHandleStrategy fieldStrategy) {
        this.fieldStrategy = fieldStrategy;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(String sceneCode) {
        this.sceneCode = sceneCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
