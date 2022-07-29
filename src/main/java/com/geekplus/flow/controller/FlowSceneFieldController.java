package com.geekplus.flow.controller;

import com.geekplus.flow.entity.FlowSceneFieldEntity;
import com.geekplus.flow.model.ApiResponse;
import com.geekplus.flow.model.FlowQueryModel;
import com.geekplus.flow.service.SceneFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("api/sceneField")
public class FlowSceneFieldController {

    @Autowired
    private SceneFieldService sceneFieldService;

    //添加场景下字段
    @PostMapping("saveOne")
    public ApiResponse saveOne(@RequestBody FlowSceneFieldEntity sceneFieldEntity) {
        return sceneFieldService.saveSceneField(sceneFieldEntity);
    }

    //更新显示字段
    @PostMapping("updateValid")
    public ApiResponse updateValid(@RequestBody FlowSceneFieldEntity sceneFieldEntity) {
        return sceneFieldService.updateValid(sceneFieldEntity);
    }

    //查询场景下的字段
    @GetMapping("list")
    public ApiResponse selectFieldByScene(String sceneCode){
        return sceneFieldService.selectFieldByScene(sceneCode);
    }

    //搜索
    @PostMapping("queryAllByScene")
    public ApiResponse queryPage(@RequestBody FlowQueryModel flowQueryModel){
        return sceneFieldService.queryAllByScene(flowQueryModel);
    }
}
