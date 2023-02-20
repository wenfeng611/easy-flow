package com.wf.flow.handler;

import com.wf.flow.handler.context.FlowContext;
import com.wf.flow.model.FlowResult;

/**
 * @author wenfeng.zhu
 * @description
 */
public interface NodeState {

    String getType();

    FlowResult handle(FlowContext context);
}
