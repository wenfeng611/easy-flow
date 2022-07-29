package com.geekplus.flow.controller;

import com.geekplus.flow.entity.FlowConfigEntity;
import com.geekplus.flow.model.ApiResponse;
import com.geekplus.flow.model.FlowQueryModel;
import com.geekplus.flow.service.FlowConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("api/flow")
public class FlowConfigController {

    @Autowired
    private FlowConfigService flowService;


    @PostMapping("saveOne")
    public ApiResponse saveOne(@RequestBody FlowConfigEntity flowEntity) {
        return flowService.saveFlow(flowEntity);
    }


    @GetMapping("findOne")
    public ApiResponse getOneById(Integer id){
        return flowService.findById(id);
    }

    //搜索
    @PostMapping("queryPage")
    public ApiResponse queryPage(@RequestBody FlowQueryModel flowQueryModel){
        return flowService.queryPage(flowQueryModel);
    }

    //启用流程
    @PostMapping("enableFlow")
    public ApiResponse updateStatus(@RequestBody FlowConfigEntity flowEntity) {
        return flowService.enableFlow(flowEntity);
    }

    //禁用流程
    @PostMapping("forbidFlow")
    public ApiResponse forbidFlow(@RequestBody FlowConfigEntity flowEntity) {
        return flowService.forbidFlow(flowEntity);
    }


    @PostMapping("update")
    public ApiResponse update(@RequestBody FlowConfigEntity flowEntity) {
        return flowService.updateFlow(flowEntity);
    }

    //更新流程图
    @PostMapping("updateFlowGraph")
    public ApiResponse updateFlowGraph(@RequestBody FlowConfigEntity flowEntity) {
        return flowService.updateFlowGraph(flowEntity);
    }
}
