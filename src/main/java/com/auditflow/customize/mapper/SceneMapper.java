package com.auditflow.customize.mapper;

import com.auditflow.customize.entity.SceneEntity;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface SceneMapper extends BaseDao<SceneEntity> {
    
    List<SceneEntity> selectAll();

    List<SceneEntity> queryPage(AuditStreamQueryModel auditStreamQueryModel);
    
    int countByQuery(AuditStreamQueryModel auditStreamQueryModel);

    void updateScene(SceneEntity sceneEntity);

    SceneEntity findByCode(String sceneCode);

    void updateValid(SceneEntity sceneEntity);
}
