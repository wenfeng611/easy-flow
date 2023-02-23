package com.wf.flow.engine.node.handler;



import com.wf.flow.context.FlowContext;
import com.wf.flow.model.FlowResult;

/**
 * @author wenfeng.zhu
 * @description
 */
public interface NodeState {

    String getType();

    FlowResult handle(FlowContext context);
}
