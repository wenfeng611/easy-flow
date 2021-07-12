package com.auditflow.customize.controller.auditstream;

import com.auditflow.customize.entity.AuditFlowEntity;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import com.auditflow.customize.service.auditstream.AuditFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("audit/flow")
public class AuditFlowController {

    @Autowired
    private AuditFlowService auditFlowService;

    //添加审核流
    @PostMapping("saveOne")
    public ApiResponse saveOne(@RequestBody AuditFlowEntity auditFlowEntity) {
        return auditFlowService.saveFlow(auditFlowEntity);
    }


    @GetMapping("findOne")
    public ApiResponse getOneById(Integer id){
        return auditFlowService.findById(id);
    }

    //搜索
    @PostMapping("queryPage")
    public ApiResponse queryPage(@RequestBody AuditStreamQueryModel auditStreamQueryModel){
        return auditFlowService.queryPage(auditStreamQueryModel);
    }

    //启用流程
    @PostMapping("enableFlow")
    public ApiResponse updateStatus(@RequestBody AuditFlowEntity auditFlowEntity) {
        return auditFlowService.enableFlow(auditFlowEntity);
    }

    //禁用流程
    @PostMapping("forbidFlow")
    public ApiResponse forbidFlow(@RequestBody AuditFlowEntity auditFlowEntity) {
        return auditFlowService.forbidFlow(auditFlowEntity);
    }

    //更新审核场景
    @PostMapping("update")
    public ApiResponse update(@RequestBody AuditFlowEntity auditFlowEntity) {
        return auditFlowService.updateFlow(auditFlowEntity);
    }

    //更新流程图
    @PostMapping("updateFlowGraph")
    public ApiResponse updateFlowGraph(@RequestBody AuditFlowEntity auditFlowEntity) {
        return auditFlowService.updateFlowGraph(auditFlowEntity);
    }
}
