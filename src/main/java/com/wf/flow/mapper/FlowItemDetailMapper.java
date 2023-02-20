package com.wf.flow.mapper;

import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.model.FlowItemSearchModel;
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
