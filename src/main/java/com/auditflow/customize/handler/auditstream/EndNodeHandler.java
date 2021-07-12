package com.auditflow.customize.handler.auditstream;

import com.auditflow.customize.entity.AuditFlowItemDetailEntity;
import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.handler.auditstream.context.FlowContext;
import com.auditflow.customize.model.auditstream.FlowResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wenfeng.zhu
 * @description
 */

@Slf4j
@Component
public class EndNodeHandler extends AbstractStateHandler {

    @Override
    public FlowResult handle(FlowContext context) {
        log.info("businessId {} 流程结束。",context.getBusinessId());
        //记录结束
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setStatus(AuditStreamEnums.FlowAuditStatusEnums.END.getCode());
        flowResult.setBusinessId(context.getBusinessId());
        flowItemMapper.updateAuditStatusAndNodeId(AuditStreamEnums.FlowAuditStatusEnums.END.getCode(),"",context.getBusinessId());
        detailMapper.save(AuditFlowItemDetailEntity.buildDefault(context.getCurrentNode(),null,context.getBusinessId(),AuditStreamEnums.FlowAuditStatusEnums.END.getCode(),"结束"));

        return flowResult;
    }
}
