package com.auditflow.customize.controller.auditstream;

import com.auditflow.customize.entity.SceneEntity;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import com.auditflow.customize.service.auditstream.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("audit/scene")
public class SceneController {

    @Autowired
    private SceneService sceneService;

    //添加审核场景
    @PostMapping("saveOne")
    public ApiResponse saveOne(@RequestBody SceneEntity sceneEntity) {
        return sceneService.saveScene(sceneEntity);
    }

    //更新审核场景
    @PostMapping("update")
    public ApiResponse update(@RequestBody SceneEntity sceneEntity) {
        return sceneService.updateScene(sceneEntity);
    }

    //更新显示字段
    @PostMapping("updateValid")
    public ApiResponse updateValid(@RequestBody SceneEntity sceneEntity) {
        return sceneService.updateValid(sceneEntity);
    }


    @GetMapping("list")
    public ApiResponse selectAll() {
        return sceneService.selectAll();
    }


    //搜索
    @PostMapping("queryPage")
    public ApiResponse queryPage(@RequestBody AuditStreamQueryModel auditStreamQueryModel){
        return sceneService.queryPage(auditStreamQueryModel);
    }

}
