package com.auditflow.customize.mapper;

import com.auditflow.customize.entity.AuditRuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface AuditRuleMapper extends BaseDao<AuditRuleEntity>{

    //某个节点的规则
    AuditRuleEntity findByFlowIdAndNodeId(@Param("flowId") Integer flowId, @Param("nodeId") String nodeId);

    //根据nodetype查询  可以直接查出条件节点
    AuditRuleEntity findByFlowIdAndNodeType(@Param("flowId") Integer flowId, @Param("nodeType") Integer nodeType);

    //根据nodetype查询  可以直接查出条件节点
    List<AuditRuleEntity> findByFlowIdAndNodeTypeInsIn(@Param("list") List<Integer> ids, @Param("nodeType") Integer nodeType);

    void updateRule(AuditRuleEntity rule);
}
