package com.wf.flow.mapper;


import com.wf.flow.entity.FlowSceneFieldEntity;
import com.wf.flow.model.FlowQueryModel;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

public interface FlowSceneFieldMapper extends BaseDao<FlowSceneFieldEntity> {

    List<FlowSceneFieldEntity> selectFieldByScene(String sceneCode);

    void updateValid(FlowSceneFieldEntity sceneFieldEntity);

    List<FlowSceneFieldEntity> queryAllByScene(FlowQueryModel flowQueryModel);
    
    int countAllByScene(FlowQueryModel flowQueryModel);
}
