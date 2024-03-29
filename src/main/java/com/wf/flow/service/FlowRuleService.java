package com.wf.flow.service;

import com.wf.flow.context.UserInfoThreadLocalContext;
import com.wf.flow.entity.FlowRuleEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.model.ApiResponse;
import com.wf.flow.mapper.FlowRuleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/14 15:40
 */

@Service
public class FlowRuleService {

    @Resource
    private FlowRuleMapper flowRuleMapper;

    //保存规则
    public ApiResponse saveRule(FlowRuleEntity ruleEntity){
        String username = UserInfoThreadLocalContext.user().getUsername();
        Date date = new Date();
        ruleEntity.setCreateTime(date);
        ruleEntity.setUpdateTime(date);
        ruleEntity.setCreaterName(username);
        flowRuleMapper.save(ruleEntity);
        return ApiResponse.success(null);
    }


    public ApiResponse getNodeRule(Integer flowId, String nodeId) {
        FlowRuleEntity flowRuleEntity = flowRuleMapper.findByFlowIdAndNodeId(flowId,nodeId);
        return ApiResponse.success(flowRuleEntity);
    }

    public ApiResponse updateRule(FlowRuleEntity rule) {
        flowRuleMapper.updateRule(rule);
        return ApiResponse.success(null);
    }

    public List<FlowRuleEntity> findConditionRule(List<Integer> ids){
        return  flowRuleMapper.findByFlowIdAndNodeTypeInsIn(ids, FlowEnums.FlowNodeTypeEnums.CONDITION.getCode());
    }
}
