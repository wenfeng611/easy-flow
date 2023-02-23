package com.wf.flow.mapper;

import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.model.FlowQueryModel;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

public interface FlowNodeMapper extends BaseDao<FlowNodeEntity>{

    List<FlowNodeEntity> selectAllNodes();

    FlowNodeEntity findTopById(Integer id);

    FlowNodeEntity findTopByType(String type);

    List<String> findAllTypes();

    List<FlowNodeEntity> queryPage(FlowQueryModel flowQueryModel);
    
    int countByQuery(FlowQueryModel flowQueryModel);

    void updateNode(FlowNodeEntity nodeEntity);

    void updateValid(FlowNodeEntity nodeEntity);
}
