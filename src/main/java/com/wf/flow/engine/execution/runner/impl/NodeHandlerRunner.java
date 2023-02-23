package com.wf.flow.engine.execution.runner.impl;

import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.node.handler.NodeState;
import com.wf.flow.utils.HolderSelectorUtil;
import com.wf.flow.utils.ProcessLogUtil;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.enums.NodeRunnerEnum;
import com.wf.flow.model.FlowResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/2 10:23
 */

@Service
@Slf4j
public class NodeHandlerRunner extends AbstractRunner {

    public static final String METHOD_NAME = "handleBusiness";

    @Override
    public String getType() {
        return NodeRunnerEnum.NODE_HANDLER.getValue();
    }

    @Override
    public FlowResult execute(FlowContext context, FlowNodeEntity flowNodeEntity) {
        NodeState nodeState = HolderSelectorUtil.getNodeState(flowNodeEntity.getType());
        try {
            //构造节点需要的参数Map
            Method method = nodeState.getClass().getDeclaredMethod(METHOD_NAME,FlowContext.class, Map.class);
            method.setAccessible(true);
            return (FlowResult) method.invoke(nodeState,context,computeParam(context,flowNodeEntity));
        } catch (Exception e) {
            ProcessLogUtil.logWarn(log,context,"get node {} method handleBusiness exception:",context.getCurrentNodeDisplayName(),e);
            return FlowResult.fail(e.getMessage(),context.getBusinessId());
        }
    }
}
