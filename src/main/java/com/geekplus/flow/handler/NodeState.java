package com.geekplus.flow.handler;

import com.geekplus.flow.handler.context.FlowContext;
import com.geekplus.flow.model.FlowResult;

/**
 * @author wenfeng.zhu
 * @description
 */
public interface NodeState {

    String getType();

    FlowResult handle(FlowContext context);
}
