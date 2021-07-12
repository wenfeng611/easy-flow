package com.auditflow.customize.handler.auditstream;

import com.auditflow.customize.handler.auditstream.context.FlowContext;
import com.auditflow.customize.model.auditstream.FlowResult;

/**
 * @author wenfeng.zhu
 * @description
 */
public interface State {

    FlowResult handle(FlowContext context);
}
