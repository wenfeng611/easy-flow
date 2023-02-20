package com.wf.flow.service;

import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.model.ApiResponse;
import com.wf.flow.model.FlowItemSearchModel;
import com.wf.flow.mapper.FlowItemMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/23 18:05
 */

@Slf4j
@Service
public class FlowItemService {

    @Resource
    private FlowItemMapper flowItemMapper;

    public ApiResponse queryPage(FlowItemSearchModel searchModel){
        PageHelper.startPage(searchModel.getPagenum(), searchModel.getPagesize());
        List<FlowItemEntity> list =  flowItemMapper.selectFlowItems(searchModel);
        PageInfo<FlowItemEntity> pageInfo = new PageInfo(list);

        return new ApiResponse(list, (int)pageInfo.getTotal());
    }

    public ApiResponse restart(int businessId,String sceneCode){
        //判断场景

        return ApiResponse.success(null);
    }


}
