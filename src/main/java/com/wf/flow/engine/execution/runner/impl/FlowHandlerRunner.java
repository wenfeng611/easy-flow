package com.wf.flow.engine.execution.runner.impl;


import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.node.handler.NodeState;
import com.wf.flow.utils.HolderSelectorUtil;
import com.wf.flow.utils.ProcessLogUtil;
import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.enums.NodeRunnerEnum;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.lang.Strings;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/3 13:19
 */

@Service
@Slf4j
public class FlowHandlerRunner extends AbstractRunner {

    @Override
    public String getType() {
        return NodeRunnerEnum.FLOW_HANDLER.getValue();
    }

    @Override
    public FlowResult execute(FlowContext context, FlowNodeEntity flowNodeEntity) {
        FlowItemEntity flowItemEntity = flowItemMapper.findByCustomId(context.getCustomId());

        String nodeType = context.getCurrentNode().getNodeType();
        //根绝nodeType就是子流程的流程场景查找流程
        FlowConfigEntity flowConfigEntity = cacheService.getFlowConfig(nodeType);
        if(Objects.isNull(flowConfigEntity)){
            ProcessLogUtil.logWarn(log,context,"子流程节点 {} 配置错误 子流程节点需要t_flow_node表中type字段和流程场景一致",nodeType);
            return FlowResult.fail("流程节点 {} 配置错误",nodeType);
        }

        //记录父流程状态
        String parentNodes = context.getCurrentNode().getId();
        String parentFlows = String.valueOf(context.getFlow().getId());
        if(StringUtils.isNotBlank(flowItemEntity.getParentNodeIds())){
            parentNodes = Strings.join(",", parentNodes,flowItemEntity.getParentNodeIds());
        }
        if(StringUtils.isNotBlank(flowItemEntity.getParentFlowIds())){
            parentFlows = Strings.join(",", parentFlows,flowItemEntity.getParentFlowIds());
        }
        flowItemMapper.updateParentNodeFlowIds(flowItemEntity.getId(),parentNodes,parentFlows,flowConfigEntity.getId(),flowConfigEntity.getSceneCode());

        GNode gNode = graphResolver.getFirstStartNode(flowConfigEntity.getGraphJson());
        NodeState nextState = HolderSelectorUtil.getNodeState(gNode.getNodeType());

        context.setSceneCode(flowConfigEntity.getSceneCode());
        context.setFlow(flowConfigEntity);
        context.setCurrentNode(gNode);
        context.setState(nextState);
        return context.run();
    }
}
