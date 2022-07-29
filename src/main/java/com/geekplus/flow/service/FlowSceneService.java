package com.geekplus.flow.service;

import com.geekplus.flow.context.UserInfoThreadLocalContext;
import com.geekplus.flow.entity.FlowSceneEntity;
import com.geekplus.flow.model.ApiResponse;
import com.geekplus.flow.model.FlowQueryModel;
import com.geekplus.flow.mapper.FlowSceneMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/14 14:21
 */

@Service
public class FlowSceneService {

    @Resource
    private FlowSceneMapper sceneMapper;

    //保存场景
    public ApiResponse saveScene(FlowSceneEntity sceneEntity){
        FlowSceneEntity old = sceneMapper.findByCode(sceneEntity.getSceneCode());
        if(Objects.nonNull(old)){
            return new ApiResponse.Fail(null,400001,"场景code已存在,保存失败！");
        }
        String username = UserInfoThreadLocalContext.user().getUsername();
        Date date = new Date();
        sceneEntity.setCreateTime(date);
        sceneEntity.setUpdateTime(date);
        sceneEntity.setCreaterName(username);
        sceneEntity.setIsValid(1);
        sceneMapper.save(sceneEntity);
        return ApiResponse.success(null);
    }

    //编辑场景
    public ApiResponse updateScene(FlowSceneEntity sceneEntity){
        FlowSceneEntity old = sceneMapper.findByCode(sceneEntity.getSceneCode());
        if(Objects.nonNull(old) && !Objects.equals(old.getId(),sceneEntity.getId())){
            return new ApiResponse.Fail(null,400001,"场景code已存在,保存失败！");
        }

        sceneMapper.updateScene(sceneEntity);
        return ApiResponse.success(null);
    }

    //查询所有的场景
    public ApiResponse selectAll(){
        List<FlowSceneEntity> result = sceneMapper.selectAll();
        return ApiResponse.success(result);
    }

    public ApiResponse queryPage(FlowQueryModel flowQueryModel) {
        flowQueryModel.setPagenum((flowQueryModel.getPagenum() - 1) * flowQueryModel.getPagesize());
        List<FlowSceneEntity> result = sceneMapper.queryPage(flowQueryModel);
        int count = sceneMapper.countByQuery(flowQueryModel);
        return new ApiResponse(result, count);
    }

    public ApiResponse updateValid(FlowSceneEntity sceneEntity) {
        sceneMapper.updateValid(sceneEntity);
        return ApiResponse.success(null);
    }
}
