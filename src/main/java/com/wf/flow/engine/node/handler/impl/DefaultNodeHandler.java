package com.wf.flow.engine.node.handler.impl;

import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.anno.FlowNode;
import com.wf.flow.model.FlowResult;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/2 14:10
 */
@Service
@FlowNode(needInsert = false)
public class DefaultNodeHandler extends AbstractNodeStateHandler {

    public static final String DEFAULT = "default";

    @Override
    protected FlowResult handleBusiness(FlowContext context, Map<String, Object> params) {
        return null;
    }

    @Override
    public String getType() {
        return DEFAULT;
    }
}
