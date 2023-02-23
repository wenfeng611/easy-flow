package com.wf.flow.engine.trigger;


import com.wf.flow.context.FlowContext;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 16:22
 */
public interface ProcessTrigger {

    /**
     * 写触发器的时候需要实现 然后再触发器节点配置改bean
     * @return 需要唤醒的流程实例的id
     */
     List<String> flowItemNeedToCall(String triggerParam, FlowContext context);
}
