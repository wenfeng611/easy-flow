package com.wf.flow.service;

import com.wf.flow.context.UserInfoThreadLocalContext;
import com.wf.flow.entity.FlowSceneFieldEntity;
import com.wf.flow.model.ApiResponse;
import com.wf.flow.model.FlowQueryModel;
import com.wf.flow.mapper.FlowSceneFieldMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/14 14:41
 */

@Service
public class SceneFieldService {

    @Resource
    FlowSceneFieldMapper sceneFieldMapper;

    //保存场景下的字段
    public ApiResponse saveSceneField(FlowSceneFieldEntity sceneFieldEntity){
        String username = UserInfoThreadLocalContext.user().getUsername();
        Date date = new Date();
        sceneFieldEntity.setCreateTime(date);
        sceneFieldEntity.setCreaterName(username);
        sceneFieldEntity.setIsValid(1);
        sceneFieldMapper.save(sceneFieldEntity);
        return ApiResponse.success(null);
    }

    //查询场景下面的字段
    public ApiResponse selectFieldByScene(String sceneCode){
        List<FlowSceneFieldEntity> result = sceneFieldMapper.selectFieldByScene(sceneCode);
        return ApiResponse.success(result);
    }

    public ApiResponse updateValid(FlowSceneFieldEntity sceneFieldEntity) {
        sceneFieldMapper.updateValid(sceneFieldEntity);
        return ApiResponse.success(null);
    }

    //不分页
    public ApiResponse queryAllByScene(FlowQueryModel flowQueryModel) {
        List<FlowSceneFieldEntity> result = sceneFieldMapper.queryAllByScene(flowQueryModel);
        //int count = sceneFieldMapper.countAllByScene(flowQueryModel);
        return new ApiResponse(result);
    }
}
