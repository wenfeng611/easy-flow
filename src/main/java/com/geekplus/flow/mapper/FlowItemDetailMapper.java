package com.geekplus.flow.mapper;

import com.geekplus.flow.entity.FlowItemDetailEntity;
import com.geekplus.flow.model.FlowItemSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface FlowItemDetailMapper extends BaseDao<FlowItemDetailEntity>{

    List<FlowItemDetailEntity> selectFlowItemDetails(FlowItemSearchModel searchModel);
}
