package com.auditflow.customize.mapper;

import com.auditflow.customize.entity.AuditFlowItemEntity;
import com.auditflow.customize.model.auditstream.AuditFlowItemSearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface AuditFlowItemMapper extends BaseDao<AuditFlowItemEntity>{

    void updateAuditStatusAndNodeId(@Param("auditStatus") Integer auditStatus, @Param("nodeId") String currentNodeId, @Param("id") Integer id);

    void updateAuditStatusAndNodeIdAndAuditUserids(@Param("auditStatus") Integer auditStatus, @Param("nodeId") String currentNodeId, @Param("id") Integer id, @Param("jobNumbers") String jobNumbers);

    AuditFlowItemEntity findById(@Param("id") Integer id);

    int countRunningFlowItems(@Param("flowId") Integer flowId);

    void updateFlowId(@Param("id") Integer id, @Param("flowId") Integer flowId);

    List<AuditFlowItemEntity> selectFlowItems(AuditFlowItemSearchModel searchModel);

    List<AuditFlowItemEntity> selectByIds(@Param("dataList") List<Integer> dataList);
}
