package com.auditflow.customize.mapper;


import com.auditflow.customize.entity.SceneFieldEntity;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface SceneFieldMapper extends BaseDao<SceneFieldEntity> {

    List<SceneFieldEntity> selectFieldByScene(String sceneCode);

    void updateValid(SceneFieldEntity sceneFieldEntity);

    List<SceneFieldEntity> queryAllByScene(AuditStreamQueryModel auditStreamQueryModel);
    
    int countAllByScene(AuditStreamQueryModel auditStreamQueryModel);
}
