package com.geekplus.flow.handler;

import com.geekplus.flow.entity.FlowItemDetailEntity;
import com.geekplus.flow.enums.FlowEnums;
import com.geekplus.flow.handler.context.FlowContext;
import com.geekplus.flow.model.FlowResult;
import com.geekplus.flow.model.GNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 10:15
 */

@Slf4j
@Component
public class WaitingHandler extends AbstractNodeStateHandler {

    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.WAITING.getCode();
    }

    @Override
    public FlowResult handle(FlowContext context) {
        log.info("businessId {} 等待",context.getBusinessId());
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setStatus(FlowEnums.FlowItemStatusEnums.END.getCode());
        flowResult.setBusinessId(context.getBusinessId());

        GNode node = context.getCurrentNode();
        flowItemMapper.updateFlowItemStatusAndNodeId(FlowEnums.FlowItemStatusEnums.RUNNING.getCode(),node.getId(),context.getBusinessId());
        detailMapper.save(FlowItemDetailEntity.buildDefault(context.getCurrentNode(),null,context.getBusinessId(), ""));


        return flowResult;
    }

    @Override
    protected void handleBusiness(FlowContext context) { }
}
