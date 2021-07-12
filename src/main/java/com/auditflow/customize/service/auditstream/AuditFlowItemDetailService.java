package com.auditflow.customize.service.auditstream;

import com.auditflow.customize.entity.AuditFlowItemDetailEntity;
import com.auditflow.customize.entity.AuditFlowItemEntity;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditFlowItemSearchModel;
import com.auditflow.customize.mapper.AuditFlowItemDetailMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Slf4j
@Service
public class AuditFlowItemDetailService {

    @Resource
    private AuditFlowItemDetailMapper auditFlowItemDetailMapper;

    public ApiResponse queryPage(AuditFlowItemSearchModel searchModel){
        PageHelper.startPage(searchModel.getPagenum(), searchModel.getPagesize());
        List<AuditFlowItemDetailEntity> list =  auditFlowItemDetailMapper.selectFlowItemDetails(searchModel);
        PageInfo<AuditFlowItemEntity> pageInfo = new PageInfo(list);
        return new ApiResponse(list, (int)pageInfo.getTotal());
    }
}
