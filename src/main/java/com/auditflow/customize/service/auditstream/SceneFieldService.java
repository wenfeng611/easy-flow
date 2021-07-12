package com.auditflow.customize.service.auditstream;

import com.auditflow.customize.context.UserInfoThreadLocalContext;
import com.auditflow.customize.entity.SceneFieldEntity;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import com.auditflow.customize.mapper.SceneFieldMapper;
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
    SceneFieldMapper sceneFieldMapper;

    //保存场景下的字段
    public ApiResponse saveSceneField(SceneFieldEntity sceneFieldEntity){
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
        List<SceneFieldEntity> result = sceneFieldMapper.selectFieldByScene(sceneCode);
        return ApiResponse.success(result);
    }

    public ApiResponse updateValid(SceneFieldEntity sceneFieldEntity) {
        sceneFieldMapper.updateValid(sceneFieldEntity);
        return ApiResponse.success(null);
    }

    //不分页
    public ApiResponse queryAllByScene(AuditStreamQueryModel auditStreamQueryModel) {
        List<SceneFieldEntity> result = sceneFieldMapper.queryAllByScene(auditStreamQueryModel);
        //int count = sceneFieldMapper.countAllByScene(auditStreamQueryModel);
        return new ApiResponse(result);
    }
}
