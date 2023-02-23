package com.wf.flow.engine.node.handler.impl;


import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.anno.FlowNode;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.model.FlowResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description 自动生成节点数据落库
 * @date 2022/8/10 17:01
 */
@Order(4)
@Component
@FlowNode(nodeName = "lang.wms.fed.triggerNode",functionType="triggerHandler")
public class TriggerNodeHandler extends AbstractNodeStateHandler {
    @Override
    protected FlowResult handleBusiness(FlowContext context, Map<String, Object> params) {
        return null;
    }

    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.TRIGGER.getCode();
    }
}
