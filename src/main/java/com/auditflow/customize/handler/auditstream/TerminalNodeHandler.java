package com.auditflow.customize.handler.auditstream;

import com.auditflow.customize.entity.AuditFlowItemDetailEntity;
import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.handler.auditstream.context.FlowContext;
import com.auditflow.customize.model.auditstream.FlowResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wenfeng.zhu
 * @description  终止节点 虚拟的 只有人工审核 之后需要终止才会用到 业务决定
 */

@Slf4j
@Component
public class TerminalNodeHandler extends AbstractStateHandler {


    @Override
    public FlowResult handle(FlowContext context) {
        log.info("businessId {} 流程终止。",context.getBusinessId());

        //记录结束
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setStatus(AuditStreamEnums.FlowAuditStatusEnums.TERMINAL.getCode());
        flowResult.setBusinessId(context.getBusinessId());
        flowItemMapper.updateAuditStatusAndNodeId(AuditStreamEnums.FlowAuditStatusEnums.TERMINAL.getCode(),"",context.getBusinessId());

        detailMapper.save(AuditFlowItemDetailEntity.buildDefault(context.getCurrentNode(),null,context.getBusinessId(),AuditStreamEnums.FlowAuditStatusEnums.TERMINAL.getCode(),"业务终止"));
        return flowResult;
    }
}
