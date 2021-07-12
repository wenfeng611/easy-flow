package com.auditflow.customize.controller.auditstream;

import com.auditflow.customize.entity.AuditRuleEntity;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.service.auditstream.AuditRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("audit/rule")
public class AuditRuleController {

    @Autowired
    private AuditRuleService auditRuleService;

    //添加审核节点
    @PostMapping("saveOne")
    public ApiResponse saveOne(@RequestBody AuditRuleEntity rule) {
        return auditRuleService.saveRule(rule);
    }

    @PostMapping("updateRule")
    public ApiResponse updateRule(@RequestBody AuditRuleEntity rule) {
        return auditRuleService.updateRule(rule);
    }

    @GetMapping("getNodeRule")
    public ApiResponse getNodeRule( Integer flowId,String nodeId) {
        return auditRuleService.getNodeRule(flowId,nodeId);
    }

}