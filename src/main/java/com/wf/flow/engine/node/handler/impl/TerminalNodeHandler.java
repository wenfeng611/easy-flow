package com.wf.flow.engine.node.handler.impl;


import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.anno.FlowNode;
import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.model.FlowResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description  终止节点
 */

@Slf4j
@Component
@FlowNode(needInsert = false)
public class TerminalNodeHandler extends AbstractNodeStateHandler {


    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.TERMINAL.getCode();
    }

    @Override
    protected FlowResult handleBusiness(FlowContext context, Map<String, Object> params) {
       logInfo(log,context,"流程终止");

        //记录结束
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setBusinessId(context.getBusinessId());
        flowItemMapper.updateFlowItemStatusAndNodeIdWithParams(getType(),FlowEnums.FlowItemStatusEnums.TERMINAL.getCode(),"",context.getBusinessId(),null);

        detailMapper.save(FlowItemDetailEntity.buildDefault(context.getCurrentNode(),null,context.getBusinessId(), "业务终止"));
        return flowResult;
    }

}
