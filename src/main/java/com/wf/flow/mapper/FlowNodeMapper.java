package com.wf.flow.mapper;

import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.model.FlowQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface FlowNodeMapper extends BaseDao<FlowNodeEntity>{

    List<FlowNodeEntity> selectAllNodes();

    FlowNodeEntity findTopById(Integer id);
    
    List<FlowNodeEntity> queryPage(FlowQueryModel flowQueryModel);
    
    int countByQuery(FlowQueryModel flowQueryModel);

    void updateNode(FlowNodeEntity nodeEntity);

    void updateValid(FlowNodeEntity nodeEntity);
}
