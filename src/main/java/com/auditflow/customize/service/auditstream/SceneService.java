package com.auditflow.customize.service.auditstream;

import com.auditflow.customize.context.UserInfoThreadLocalContext;
import com.auditflow.customize.entity.SceneEntity;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import com.auditflow.customize.mapper.SceneMapper;
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
public class SceneService {

    @Resource
    private SceneMapper sceneMapper;

    //保存场景
    public ApiResponse saveScene(SceneEntity sceneEntity){
        SceneEntity old = sceneMapper.findByCode(sceneEntity.getSceneCode());
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
    public ApiResponse updateScene(SceneEntity sceneEntity){
        SceneEntity old = sceneMapper.findByCode(sceneEntity.getSceneCode());
        if(Objects.nonNull(old) && !Objects.equals(old.getId(),sceneEntity.getId())){
            return new ApiResponse.Fail(null,400001,"场景code已存在,保存失败！");
        }

        sceneMapper.updateScene(sceneEntity);
        return ApiResponse.success(null);
    }

    //查询所有的场景
    public ApiResponse selectAll(){
        List<SceneEntity> result = sceneMapper.selectAll();
        return ApiResponse.success(result);
    }

    public ApiResponse queryPage(AuditStreamQueryModel auditStreamQueryModel) {
        auditStreamQueryModel.setPagenum((auditStreamQueryModel.getPagenum() - 1) * auditStreamQueryModel.getPagesize());
        List<SceneEntity> result = sceneMapper.queryPage(auditStreamQueryModel);
        int count = sceneMapper.countByQuery(auditStreamQueryModel);
        return new ApiResponse(result, count);
    }

    public ApiResponse updateValid(SceneEntity sceneEntity) {
        sceneMapper.updateValid(sceneEntity);
        return ApiResponse.success(null);
    }
}
