package com.wf.flow.handler;

import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.handler.context.FlowContext;
import com.wf.flow.model.FlowResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wenfeng.zhu
 * @description
 */

@Slf4j
@Component
public class EndNodeHandler extends AbstractNodeStateHandler {

    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.END.getCode();
    }

    @Override
    public FlowResult handle(FlowContext context) {
        log.info("businessId {} 流程结束。",context.getBusinessId());
        //记录结束
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setStatus(FlowEnums.FlowItemStatusEnums.END.getCode());
        flowResult.setBusinessId(context.getBusinessId());
        flowItemMapper.updateFlowItemStatusAndNodeId(FlowEnums.FlowItemStatusEnums.END.getCode(),"",context.getBusinessId());
        detailMapper.save(FlowItemDetailEntity.buildDefault(context.getCurrentNode(),null,context.getBusinessId(), "结束"));

        return flowResult;
    }

    @Override
    protected void handleBusiness(FlowContext context) { }
}
