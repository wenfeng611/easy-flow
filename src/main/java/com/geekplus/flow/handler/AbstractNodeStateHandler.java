package com.geekplus.flow.handler;

import com.geekplus.flow.entity.FlowItemDetailEntity;
import com.geekplus.flow.enums.FlowEnums;
import com.geekplus.flow.handler.context.FlowContext;
import com.geekplus.flow.handler.holder.StateHandlerHolderService;
import com.geekplus.flow.mapper.*;
import com.geekplus.flow.model.FlowResult;
import com.geekplus.flow.model.GNode;
import com.geekplus.flow.service.FlowRuleService;
import com.geekplus.flow.utils.graph.GraphResolvor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author wenfeng.zhu
 * @description
 */

@Slf4j
public abstract class AbstractNodeStateHandler implements NodeState {

    public static final String FLOW_NOT_FOUNT = "未找到流程";
  
    @Resource
    protected FlowConfigMapper flowMapper;

    @Resource
    protected FlowItemMapper flowItemMapper;

    @Resource
    protected FlowItemDetailMapper detailMapper;

    @Resource
    protected FlowRuleMapper flowRuleMapper;

    @Resource
    protected FlowNodeMapper nodeMapper;

    @Autowired
    protected FlowRuleService ruleService;

    @Autowired
    protected StateHandlerHolderService stateHandlerHolderService;


    //根据node找到下一个处理的handler
    public NodeState chooseNextHandler(GNode node){
      //根据节点类型找到下一个处理的节点
       return stateHandlerHolderService.getStateHandler(String.valueOf(node.getNodeType()));
    }

    @Override
    public FlowResult handle(FlowContext context) {
        log.info("AbstractNodeStateHandler handleBusiness");
        handleBusiness(context);

        String nodeId = context.getCurrentNode().getId();
        GNode firstNextNodeBYG6NodeId = GraphResolvor.getFirstNextNodeBYG6NodeId(context.getFlow().getGraphJson(), nodeId);
        NodeState nextState = chooseNextHandler(firstNextNodeBYG6NodeId);
        //更新context里面的值
        flowItemMapper.updateFlowItemStatusAndNodeId(FlowEnums.FlowItemStatusEnums.RUNNING.getCode(),nodeId,context.getBusinessId());
        detailMapper.save(FlowItemDetailEntity.buildDefault(context.getCurrentNode(),firstNextNodeBYG6NodeId,context.getBusinessId(),""));

        context.setCurrentNode(firstNextNodeBYG6NodeId);
        context.setState(nextState);
        return context.getState().handle(context);
    }

    protected abstract void handleBusiness(FlowContext context);
}
