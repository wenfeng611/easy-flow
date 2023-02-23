package com.wf.flow.engine.execution.runner.impl;


import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.cache.service.CacheService;
import com.wf.flow.engine.execution.runner.NodeRunner;
import com.wf.flow.engine.graph.GraphResolverService;
import com.wf.flow.engine.groovy.GroovyService;
import com.wf.flow.engine.service.FlowProcessService;
import com.wf.flow.utils.NodeConfigParseTool;
import com.wf.flow.utils.ParamGetTool;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.entity.FlowRuleEntity;
import com.wf.flow.mapper.FlowItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/9 19:25
 */
public abstract class AbstractRunner implements NodeRunner {

    @Autowired
    protected FlowItemMapper flowItemMapper;

    @Autowired
    protected GroovyService groovyService;

    @Autowired
    protected GraphResolverService graphResolver;

    @Autowired
    protected FlowProcessService flowProcessService;

    @Autowired
    protected CacheService cacheService;

    public Map<String, Object> computeParam(FlowContext context, FlowNodeEntity flowNodeEntity){
        Map<String, Object> result = new HashMap();
        if(Objects.isNull(context.getFlow()) || Objects.isNull(context.getCurrentNode())){
            return result;
        }
        FlowRuleEntity nodeRule = cacheService.getFlowNodeRule(context.getFlow().getId(), context.getCurrentNode().getId());
        if(Objects.isNull(nodeRule)){
            return result;
        }
        Map<String, String> nodeFieldMappings = NodeConfigParseTool.getNodeFieldMappings(nodeRule.getAutoJudgeRule());
        if(StringUtils.isNotBlank(flowNodeEntity.getInputParams())){
            Arrays.stream(flowNodeEntity.getInputParams().split(",")).forEach(field->{
                //判断设置了值 防止策略里面的字段影响到具体节点的字段
                if(nodeFieldMappings.containsKey(field)){
                    result.put(field, ParamGetTool.getFieldValueFromContextFirst(nodeFieldMappings.get(field),context));
                }
            });
        }
        return result;
    }
}
