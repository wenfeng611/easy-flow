package com.auditflow.customize.mapper;

import com.auditflow.customize.entity.AuditFlowItemDetailEntity;
import com.auditflow.customize.model.auditstream.AuditFlowItemSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Mapper
public interface AuditFlowItemDetailMapper extends BaseDao<AuditFlowItemDetailEntity>{

    List<AuditFlowItemDetailEntity> selectFlowItemDetails(AuditFlowItemSearchModel searchModel);
}
