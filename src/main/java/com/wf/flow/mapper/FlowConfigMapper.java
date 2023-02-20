package com.wf.flow.mapper;

import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.model.FlowQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface FlowConfigMapper extends BaseDao<FlowConfigEntity>{

    List<FlowConfigEntity> findBySceneCode(String sceneCode);
    
    FlowConfigEntity findTopById(Integer id);
    
    List<FlowConfigEntity> queryPage(FlowQueryModel flowQueryModel);
    
    int countByQuery(FlowQueryModel flowQueryModel);

    void updateStatus(FlowConfigEntity flowConfigEntity);

    void updateFlow(FlowConfigEntity flowConfigEntity);

    void updateFlowGraph(FlowConfigEntity flowConfigEntity);
}
