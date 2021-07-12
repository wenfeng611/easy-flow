package com.auditflow.customize.controller.auditstream;

import com.auditflow.customize.entity.SceneFieldEntity;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import com.auditflow.customize.service.auditstream.SceneFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("audit/sceneField")
public class SceneFieldController {

    @Autowired
    private SceneFieldService sceneFieldService;

    //添加审核场景下字段
    @PostMapping("saveOne")
    public ApiResponse saveOne(@RequestBody SceneFieldEntity sceneFieldEntity) {
        return sceneFieldService.saveSceneField(sceneFieldEntity);
    }

    //更新显示字段
    @PostMapping("updateValid")
    public ApiResponse updateValid(@RequestBody SceneFieldEntity sceneFieldEntity) {
        return sceneFieldService.updateValid(sceneFieldEntity);
    }

    //查询场景下的字段
    @GetMapping("list")
    public ApiResponse selectFieldByScene(String sceneCode){
        return sceneFieldService.selectFieldByScene(sceneCode);
    }

    //搜索
    @PostMapping("queryAllByScene")
    public ApiResponse queryPage(@RequestBody AuditStreamQueryModel auditStreamQueryModel){
        return sceneFieldService.queryAllByScene(auditStreamQueryModel);
    }
}
