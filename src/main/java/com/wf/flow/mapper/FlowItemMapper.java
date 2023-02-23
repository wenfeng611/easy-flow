package com.wf.flow.mapper;

import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.model.FlowItemSearchModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

public interface FlowItemMapper extends BaseDao<FlowItemEntity>{

    void updateFlowItemStatusAndNodeId(@Param("flowStatus") Integer flowStatus, @Param("nodeId") String currentNodeId, @Param("id") Integer id);

    void updateFlowItemStatusAndNodeIdWithParams(@Param("nodeType") String nodeType, @Param("flowStatus") Integer flowStatus, @Param("nodeId") String currentNodeId, @Param("id") Integer id,
                                                 @Param("paramsJson") String paramsJson);

    FlowItemEntity findById(@Param("id") Integer id);

    FlowItemEntity findByCustomId(@Param("customId") String customId);

    int countRunningFlowItems(@Param("flowId") Integer flowId);

    void updateFlowId(@Param("id") Integer id, @Param("flowId") Integer flowId);

    void updateParentNodeFlowIds(@Param("id") Integer id,
                                 @Param("parentNodeIds") String parentNodeIds,
                                 @Param("parentFlowIds") String parentFlowIds,
                                 @Param("currentFlowId") Integer flowId,
                                 @Param("sceneCode") String sceneCode);

    List<FlowItemEntity> selectFlowItems(FlowItemSearchModel searchModel);

    long countFlowItems(FlowItemSearchModel searchModel);

    List<FlowItemEntity> selectByIds(@Param("dataList") List<Integer> dataList);
}
