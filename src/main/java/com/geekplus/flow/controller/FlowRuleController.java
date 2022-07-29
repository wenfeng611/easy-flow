package com.geekplus.flow.controller;

import com.geekplus.flow.entity.FlowRuleEntity;
import com.geekplus.flow.model.ApiResponse;
import com.geekplus.flow.service.FlowRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("api/rule")
public class FlowRuleController {

    @Autowired
    private FlowRuleService flowRuleService;

    //添加节点
    @PostMapping("saveOne")
    public ApiResponse saveOne(@RequestBody FlowRuleEntity rule) {
        return flowRuleService.saveRule(rule);
    }

    @PostMapping("updateRule")
    public ApiResponse updateRule(@RequestBody FlowRuleEntity rule) {
        return flowRuleService.updateRule(rule);
    }

    @GetMapping("getNodeRule")
    public ApiResponse getNodeRule( Integer flowId,String nodeId) {
        return flowRuleService.getNodeRule(flowId,nodeId);
    }

}