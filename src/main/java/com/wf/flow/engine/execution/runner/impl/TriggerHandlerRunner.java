package com.wf.flow.engine.execution.runner.impl;

import com.alibaba.fastjson.JSON;
import com.wf.flow.context.FlowContext;
import com.wf.flow.enums.FunctionTypeEnum;
import com.wf.flow.engine.trigger.ProcessTrigger;
import com.wf.flow.utils.NodeConfigParseTool;
import com.wf.flow.utils.ProcessLogUtil;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.entity.FlowRuleEntity;
import com.wf.flow.enums.NodeRunnerEnum;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.trigger.TriggerConfig;
import com.wf.flow.utils.JSONUtil;
import com.wf.flow.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author wenfeng.zhu
 * @description 触发节点的处理 用于从当前流程触发其他流程的等待节点
 * @date 2022/8/10 15:39
 */

@Service
@Slf4j
public class TriggerHandlerRunner extends AbstractRunner {

    public Executor asyncExecutor = Executors.newFixedThreadPool(5);

    @Override
    public String getType() {
        return NodeRunnerEnum.TRIGGER_HANDLER.getValue();
    }

    @Override
    public FlowResult execute(FlowContext context, FlowNodeEntity flowNodeEntity) {
        FlowResult flowResult = FlowResult.ok(context.getBusinessId(), new HashMap());

        FlowRuleEntity waitNodeRule = cacheService.getFlowNodeRule(context.getFlow().getId(), context.getCurrentNode().getId());
        if(Objects.isNull(waitNodeRule)){
            return flowResult;
        }
        TriggerConfig triggerConfig = NodeConfigParseTool.getNodeConfigEntity(waitNodeRule.getAutoJudgeRule(), TriggerConfig.class);
        FunctionTypeEnum functionType = FunctionTypeEnum.of(triggerConfig.getFunctionType());
        if(Objects.isNull(functionType)) return flowResult;

        List<String> waitCallFlowIds = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("context",context);
        params.put("triggerParam",triggerConfig.getTriggerParam());
        switch (functionType){
            case GROOVY:
                waitCallFlowIds = handleGroovyTrigger(triggerConfig.getContent(),params);
                break;
            case SPRING_BEAN:
                waitCallFlowIds = handleSpringBeanTrigger(triggerConfig.getContent(),params);
                break;
            default:
        }
        if(CollectionUtils.isEmpty(waitCallFlowIds)){
            return flowResult;
        }
       //触发其他流程
        String eventName = triggerConfig.getEvent();
        final FlowContext flowContext = JSON.parseObject(JSON.toJSONString(context),FlowContext.class);
        for (String waitCallFlowId : waitCallFlowIds) {
            CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    ProcessLogUtil.logInfo(log,flowContext," Trigger event {} 触发流程：{}",eventName,waitCallFlowId);
                    FlowResult continueResult= flowProcessService.continueFlow(flowContext.getMainSceneCode(),waitCallFlowId,"自动触发",eventName,null,new HashMap<>());
                    ProcessLogUtil.logInfo(log,flowContext," Trigger event {} 触发流程：{} 结果：{}",eventName,waitCallFlowId, JSONUtil.objToJson(continueResult));

                }
            },asyncExecutor);

        }
        return flowResult;
    }

    private List<String> handleSpringBeanTrigger(String content, Map<String, Object> params) {
        ProcessTrigger processTrigger = (ProcessTrigger)SpringContextUtils.getBean(content);
        return processTrigger.flowItemNeedToCall((String) params.get("triggerParam"),(FlowContext) params.get("context"));
    }

    private List<String> handleGroovyTrigger(String content, Map<String, Object> params) {
        Object result = groovyService.execute(content, params);
        if(result instanceof List){
            return (List<String>)result;
        }
        return new ArrayList<>();
    }
}
