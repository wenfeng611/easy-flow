package com.auditflow.customize.service.auditstream;

import com.auditflow.customize.context.UserInfoThreadLocalContext;
import com.auditflow.customize.entity.AuditRuleEntity;
import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.mapper.AuditRuleMapper;
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
public class AuditRuleService {

    @Resource
    private AuditRuleMapper auditRuleMapper;

    //保存规则
    public ApiResponse saveRule(AuditRuleEntity ruleEntity){
        String username = UserInfoThreadLocalContext.user().getUsername();
        Date date = new Date();
        ruleEntity.setCreateTime(date);
        ruleEntity.setUpdateTime(date);
        ruleEntity.setCreaterName(username);
        auditRuleMapper.save(ruleEntity);
        return ApiResponse.success(null);
    }


    public ApiResponse getNodeRule(Integer flowId, String nodeId) {
        AuditRuleEntity auditRuleEntity = auditRuleMapper.findByFlowIdAndNodeId(flowId,nodeId);
        return ApiResponse.success(auditRuleEntity);
    }

    public ApiResponse updateRule(AuditRuleEntity rule) {
        auditRuleMapper.updateRule(rule);
        return ApiResponse.success(null);
    }

    public List<AuditRuleEntity> findConditionRule(List<Integer> ids){
        return  auditRuleMapper.findByFlowIdAndNodeTypeInsIn(ids, AuditStreamEnums.FlowNodeTypeEnums.CONDITION.getCode());
    }
}
