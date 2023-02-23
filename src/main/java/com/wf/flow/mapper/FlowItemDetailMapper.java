package com.wf.flow.mapper;



import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.model.FlowItemSearchModel;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

public interface FlowItemDetailMapper extends BaseDao<FlowItemDetailEntity>{

    List<FlowItemDetailEntity> selectFlowItemDetails(FlowItemSearchModel searchModel);

    long countFlowItemDetails(FlowItemSearchModel searchModel);
}
