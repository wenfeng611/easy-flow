package com.wf.flow.service;

import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.model.ApiResponse;
import com.wf.flow.model.FlowItemSearchModel;
import com.wf.flow.mapper.FlowItemDetailMapper;
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
public class FlowItemDetailService {

    @Resource
    private FlowItemDetailMapper flowItemDetailMapper;

    public ApiResponse queryPage(FlowItemSearchModel searchModel){
        PageHelper.startPage(searchModel.getPagenum(), searchModel.getPagesize());
        List<FlowItemDetailEntity> list =  flowItemDetailMapper.selectFlowItemDetails(searchModel);
        PageInfo<FlowItemEntity> pageInfo = new PageInfo(list);
        return new ApiResponse(list, (int)pageInfo.getTotal());
    }
}
