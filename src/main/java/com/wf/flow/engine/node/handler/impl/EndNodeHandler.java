package com.wf.flow.engine.node.handler.impl;


import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.anno.FlowNode;
import com.wf.flow.engine.node.handler.NodeState;
import com.wf.flow.utils.HolderSelectorUtil;
import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 */

@Slf4j
@Component
@Order(2)
@FlowNode(nodeName = "lang.wms.fed.endNode",executeType = 0)
public class EndNodeHandler extends AbstractNodeStateHandler {

    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.END.getCode();
    }

    @Override
    protected FlowResult handleBusiness(FlowContext context, Map<String, Object> params) {

        //判断是否是子流程结束节点
        FlowItemEntity flowItemEntity = flowItemMapper.findByCustomId(context.getCustomId());
        if(StringUtils.isNotBlank(flowItemEntity.getParentNodeIds())){
            //找到父节点的下一个节点执行
            String[] parentNodeIds = flowItemEntity.getParentNodeIds().split(",");
            String[] parentFlowIds = flowItemEntity.getParentFlowIds().split(",");
            String nodeG6Id = parentNodeIds[0];

            FlowConfigEntity parentFlow = flowMapper.findTopById(Integer.parseInt(parentFlowIds[0]));
            context.setFlow(parentFlow);
            context.setSceneCode(parentFlow.getSceneCode());

            GNode gNode = graphResolver.getFirstNextNodeBYG6NodeId(context,nodeG6Id);
            NodeState nextState = HolderSelectorUtil.getNodeState(gNode.getNodeType());

            context.setCurrentNode(gNode);
            context.setState(nextState);

            flowItemMapper.updateParentNodeFlowIds(flowItemEntity.getId(),removeFirstElement(parentNodeIds),removeFirstElement(parentFlowIds),parentFlow.getId(),parentFlow.getSceneCode());

            logInfo(log,context,"继续执行执行父节点流程 {}",context.getCurrentNodeDisplayName());
            return context.run();
        }

        logInfo(log,context,"流程结束");
        //记录结束
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setBusinessId(context.getBusinessId());
        flowItemMapper.updateFlowItemStatusAndNodeIdWithParams(getType(),FlowEnums.FlowItemStatusEnums.END.getCode(),"",context.getBusinessId(),null);
        detailMapper.save(FlowItemDetailEntity.buildDefault(context.getCurrentNode(),null,context.getBusinessId(), "结束"));

        return flowResult;
    }


    private String removeFirstElement(String[] arr){
        int length = arr.length;
        if(length == 1){
            return Strings.EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < length; i++) {
            stringBuilder.append(arr[1]);
            if(i < length-1){
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }
}
