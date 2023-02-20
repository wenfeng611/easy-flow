package com.wf.flow.handler.context;

import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.handler.NodeState;
import com.wf.flow.handler.StartHandler;
import com.wf.flow.handler.holder.StateHandlerHolderService;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import com.wf.flow.service.strage.IFlowHandleStrategy;
import com.wf.flow.utils.SpringContextUtils;

/**
 * @author wenfeng.zhu
 * @description   流程状态记录状态对象
 */
public class FlowContext {

    private NodeState state;

    private String sceneCode;
    private IFlowHandleStrategy fieldStrategy;

    private GNode currentNode;
    private FlowConfigEntity flow;
    private Integer businessId;

    private String username;

    private Object businessData;

    //默认创建Condition处理状态
    public FlowContext(IFlowHandleStrategy fieldStrategy, String sceneCode, String username) {
        this.fieldStrategy = fieldStrategy;
        this.sceneCode = sceneCode;
        this.username = username;
        this.state=  SpringContextUtils.getBean(StartHandler.class);
    }

    public FlowContext(IFlowHandleStrategy fieldStrategy, String sceneCode, GNode node, Integer businessId, String username) {
        this.fieldStrategy = fieldStrategy;
        this.sceneCode = sceneCode;
        this.currentNode= node;
        this.businessId=businessId;
        this.username=username;
        StateHandlerHolderService stateHandlerHolderService = SpringContextUtils.getBean(StateHandlerHolderService.class);
        this.state = stateHandlerHolderService.getStateHandler(node.getNodeType());
    }

    public FlowContext(IFlowHandleStrategy fieldStrategy, String sceneCode, GNode node, Integer businessId, FlowConfigEntity flow, String username) {
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

    public FlowConfigEntity getFlow() {
        return flow;
    }

    public void setFlow(FlowConfigEntity flow) {
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

    public NodeState getState() {
        return state;
    }

    public void setState(NodeState state) {
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

    public Object getBusinessData() {
        return businessData;
    }

    public void setBusinessData(Object businessData) {
        this.businessData = businessData;
    }
}