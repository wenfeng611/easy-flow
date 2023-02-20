package com.wf.flow.handler;

import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.handler.context.FlowContext;
import com.wf.flow.model.FlowResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wenfeng.zhu
 * @description  终止节点
 */

@Slf4j
@Component
public class TerminalNodeHandler extends AbstractNodeStateHandler {


    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.TERMINAL.getCode();
    }

    @Override
    public FlowResult handle(FlowContext context) {
        log.info("businessId {} 流程终止。",context.getBusinessId());

        //记录结束
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setStatus(FlowEnums.FlowItemStatusEnums.TERMINAL.getCode());
        flowResult.setBusinessId(context.getBusinessId());
        flowItemMapper.updateFlowItemStatusAndNodeId(FlowEnums.FlowItemStatusEnums.TERMINAL.getCode(),"",context.getBusinessId());

        detailMapper.save(FlowItemDetailEntity.buildDefault(context.getCurrentNode(),null,context.getBusinessId(), "业务终止"));
        return flowResult;
    }

    @Override
    protected void handleBusiness(FlowContext context) { }
}
