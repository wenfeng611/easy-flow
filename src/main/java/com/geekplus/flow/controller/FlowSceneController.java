package com.geekplus.flow.controller;

import com.geekplus.flow.entity.FlowSceneEntity;
import com.geekplus.flow.model.ApiResponse;
import com.geekplus.flow.model.FlowQueryModel;
import com.geekplus.flow.service.FlowSceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("api/scene")
public class FlowSceneController {

    @Autowired
    private FlowSceneService sceneService;

    //添加场景
    @PostMapping("saveOne")
    public ApiResponse saveOne(@RequestBody FlowSceneEntity sceneEntity) {
        return sceneService.saveScene(sceneEntity);
    }

    //更新场景
    @PostMapping("update")
    public ApiResponse update(@RequestBody FlowSceneEntity sceneEntity) {
        return sceneService.updateScene(sceneEntity);
    }

    //更新显示字段
    @PostMapping("updateValid")
    public ApiResponse updateValid(@RequestBody FlowSceneEntity sceneEntity) {
        return sceneService.updateValid(sceneEntity);
    }


    @GetMapping("list")
    public ApiResponse selectAll() {
        return sceneService.selectAll();
    }


    //搜索
    @PostMapping("queryPage")
    public ApiResponse queryPage(@RequestBody FlowQueryModel flowQueryModel){
        return sceneService.queryPage(flowQueryModel);
    }

}
