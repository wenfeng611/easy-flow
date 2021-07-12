package com.auditflow.customize.mapper;

import com.auditflow.customize.entity.NodeEntity;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface NodeMapper extends BaseDao<NodeEntity>{

    List<NodeEntity> selectAllNodes();

    NodeEntity findTopById(Integer id);
    
    List<NodeEntity> queryPage(AuditStreamQueryModel auditStreamQueryModel);
    
    int countByQuery(AuditStreamQueryModel auditStreamQueryModel);

    void updateNode(NodeEntity nodeEntity);

    void updateValid(NodeEntity nodeEntity);
}
