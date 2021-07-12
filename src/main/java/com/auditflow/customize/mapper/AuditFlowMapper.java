package com.auditflow.customize.mapper;

import com.auditflow.customize.entity.AuditFlowEntity;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface AuditFlowMapper extends BaseDao<AuditFlowEntity>{

    List<AuditFlowEntity> findBySceneCode(String sceneCode);
    
    AuditFlowEntity findTopById(Integer id);
    
    List<AuditFlowEntity> queryPage(AuditStreamQueryModel auditStreamQueryModel);
    
    int countByQuery(AuditStreamQueryModel auditStreamQueryModel);

    void updateStatus(AuditFlowEntity auditFlowEntity);

    void updateFlow(AuditFlowEntity auditFlowEntity);

    void updateFlowGraph(AuditFlowEntity auditFlowEntity);
}
