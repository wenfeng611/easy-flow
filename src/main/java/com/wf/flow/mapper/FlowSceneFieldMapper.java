package com.wf.flow.mapper;


import com.wf.flow.entity.FlowSceneFieldEntity;
import com.wf.flow.model.FlowQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface FlowSceneFieldMapper extends BaseDao<FlowSceneFieldEntity> {

    List<FlowSceneFieldEntity> selectFieldByScene(String sceneCode);

    void updateValid(FlowSceneFieldEntity sceneFieldEntity);

    List<FlowSceneFieldEntity> queryAllByScene(FlowQueryModel flowQueryModel);
    
    int countAllByScene(FlowQueryModel flowQueryModel);
}
