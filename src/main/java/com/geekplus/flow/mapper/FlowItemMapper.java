package com.geekplus.flow.mapper;

import com.geekplus.flow.entity.FlowItemEntity;
import com.geekplus.flow.model.FlowItemSearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface FlowItemMapper extends BaseDao<FlowItemEntity>{

    void updateFlowItemStatusAndNodeId(@Param("flow_status") Integer flowStatus, @Param("nodeId") String currentNodeId, @Param("id") Integer id);

    FlowItemEntity findById(@Param("id") Integer id);

    int countRunningFlowItems(@Param("flowId") Integer flowId);

    void updateFlowId(@Param("id") Integer id, @Param("flowId") Integer flowId);

    List<FlowItemEntity> selectFlowItems(FlowItemSearchModel searchModel);

    List<FlowItemEntity> selectByIds(@Param("dataList") List<Integer> dataList);
}
