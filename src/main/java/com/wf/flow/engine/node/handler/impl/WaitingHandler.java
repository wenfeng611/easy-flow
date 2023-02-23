package com.wf.flow.engine.node.handler.impl;

import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.anno.FlowNode;
import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 10:15
 */

@Slf4j
@Order(3)
@Component
@FlowNode(nodeName = "lang.wms.fed.waitingNode",executeType = 0)
public class WaitingHandler extends AbstractNodeStateHandler {

    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.WAITING.getCode();
    }

    @Override
    protected FlowResult handleBusiness(FlowContext context, Map<String, Object> params) {
        logInfo(log,context,"等待节点：{}",context.getCurrentNodeDisplayName());
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setBusinessId(context.getBusinessId());

        GNode node = context.getCurrentNode();
        flowItemMapper.updateFlowItemStatusAndNodeIdWithParams(getType(),FlowEnums.FlowItemStatusEnums.RUNNING.getCode(),node.getId(),context.getBusinessId(),null);
        detailMapper.save(FlowItemDetailEntity.buildDefault(context.getCurrentNode(),null,context.getBusinessId(), ""));


        return flowResult;
    }
}
