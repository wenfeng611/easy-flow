package com.wf.flow.engine.cache.service;


import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.entity.FlowRuleEntity;
import groovy.lang.GroovyObject;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/11 13:08
 */
public interface CacheService {

    FlowNodeEntity getNode(String nodeType);

    FlowConfigEntity getFlowConfig(String sceneCode);

    FlowRuleEntity getFlowNodeRule(Integer flowId, String g6ElementId);

    List<FlowRuleEntity> getFlowNodeRules(Integer flowId, List<String> edges);

    GroovyObject getGroovy(String scriptContent);
}
