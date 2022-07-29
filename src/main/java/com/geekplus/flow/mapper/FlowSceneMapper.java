package com.geekplus.flow.mapper;

import com.geekplus.flow.entity.FlowSceneEntity;
import com.geekplus.flow.model.FlowQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface FlowSceneMapper extends BaseDao<FlowSceneEntity> {
    
    List<FlowSceneEntity> selectAll();

    List<FlowSceneEntity> queryPage(FlowQueryModel flowQueryModel);
    
    int countByQuery(FlowQueryModel flowQueryModel);

    void updateScene(FlowSceneEntity sceneEntity);

    FlowSceneEntity findByCode(String sceneCode);

    void updateValid(FlowSceneEntity sceneEntity);
}
