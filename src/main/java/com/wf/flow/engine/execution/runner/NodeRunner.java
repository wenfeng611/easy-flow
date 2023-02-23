package com.wf.flow.engine.execution.runner;

import com.wf.flow.context.FlowContext;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.model.FlowResult;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/2 10:22
 */
public interface NodeRunner {

    String getType();

    FlowResult execute(FlowContext context, FlowNodeEntity flowNodeEntity);
}
