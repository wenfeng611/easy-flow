package com.wf.flow.engine.cache.service.impl;

import com.wf.flow.engine.cache.manager.ProcessCacheManager;
import com.wf.flow.engine.cache.service.CacheService;
import com.wf.flow.enums.CacheType;
import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.entity.FlowRuleEntity;
import com.wf.flow.mapper.FlowConfigMapper;
import com.wf.flow.mapper.FlowNodeMapper;
import com.wf.flow.mapper.FlowRuleMapper;
import groovy.lang.GroovyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/11 13:08
 */

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private ProcessCacheManager cacheManager;

    @Autowired
    private FlowNodeMapper nodeMapper;

    @Autowired
    private FlowConfigMapper flowConfigMapper;

    @Autowired
    private FlowRuleMapper ruleMapper;

    @Override
    public FlowNodeEntity getNode(String nodeType){
        return cacheManager.get(CacheType.NODE.name(), nodeType,() -> nodeMapper.findTopByType(nodeType));
    }

    @Override
    public FlowConfigEntity getFlowConfig(String sceneCode){
        return cacheManager.get(CacheType.FLOW_CONFIG.name(), sceneCode,() -> flowConfigMapper.findBySceneCode(sceneCode));
    }

    @Override
    public FlowRuleEntity getFlowNodeRule(Integer flowId, String g6ElementId){
        String flowIdStr = String.valueOf(flowId);
       return cacheManager.get(CacheType.RULE.name(), flowIdStr.concat(g6ElementId),() -> ruleMapper.findByFlowIdAndNodeId(flowId,g6ElementId));
    }

    @Override
    public List<FlowRuleEntity> getFlowNodeRules(Integer flowId, List<String> g6ElementIds){
        List<FlowRuleEntity> result = new ArrayList<>();
        for (String g6ElementId : g6ElementIds) {
            FlowRuleEntity ruleEntity = getFlowNodeRule(flowId,g6ElementId);
            if(Objects.nonNull(ruleEntity)){
                result.add(ruleEntity);
            }
        }
        return result;
    }

    @Override
    public GroovyObject getGroovy(String scriptContent) {
        return cacheManager.computeIfAbsent(CacheType.SCRIPT.name(), scriptContent);
    }
}
