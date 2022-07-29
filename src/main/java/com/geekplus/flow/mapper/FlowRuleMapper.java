package com.geekplus.flow.mapper;

import com.geekplus.flow.entity.FlowRuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface FlowRuleMapper extends BaseDao<FlowRuleEntity>{

    //某个节点的规则
    FlowRuleEntity findByFlowIdAndNodeId(@Param("flowId") Integer flowId, @Param("nodeId") String nodeId);

    //根据nodetype查询  可以直接查出条件节点
    FlowRuleEntity findByFlowIdAndNodeType(@Param("flowId") Integer flowId, @Param("nodeType") String nodeType);

    //根据nodetype查询  可以直接查出条件节点
    List<FlowRuleEntity> findByFlowIdAndNodeTypeInsIn(@Param("list") List<Integer> ids, @Param("nodeType") String nodeType);

    void updateRule(FlowRuleEntity rule);
}
