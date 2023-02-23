package com.wf.flow.engine.execution.runner.impl;


import com.wf.flow.context.FlowContext;
import com.wf.flow.utils.ProcessLogUtil;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.enums.NodeRunnerEnum;
import com.wf.flow.model.FlowResult;
import com.wf.flow.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/2 11:06
 */
@Service
@Slf4j
public class GroovyNodeRunner extends AbstractRunner {

    @Override
    public String getType() {
        return NodeRunnerEnum.GROOVY.getValue();
    }

    @Override
    public FlowResult execute(FlowContext context, FlowNodeEntity flowNodeEntity) {
        Map<String, Object> stringObjectMap = computeParam(context, flowNodeEntity);
        stringObjectMap.put("context",context);
        try{
            Object result = groovyService.execute(flowNodeEntity.getScriptContent(),stringObjectMap);
            ProcessLogUtil.logInfo(log,context,"groovy script result: {}", JSONUtil.objToJson(result));
            if(result instanceof FlowResult){
                return (FlowResult)result;
            }

            Map<String, Object> param = new HashMap();
            if(Objects.nonNull(result)){
                param = JSONUtil.jsonToObj(JSONUtil.objToJson(result),Map.class);
            }
            return FlowResult.ok(context.getBusinessId(),param);
        }catch (Exception e){
            ProcessLogUtil.logError(log,context,"groovy execute fail",e);
            return FlowResult.fail("fail",context.getBusinessId());
        }
    }
}
