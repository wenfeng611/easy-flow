package com.auditflow.customize.controller.auditstream;

import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditFlowItemSearchModel;
import com.auditflow.customize.service.auditstream.AuditFlowItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("audit/flowitem")
public class AuditFlowItemController {

    @Autowired
    private AuditFlowItemService auditFlowItemService;


    @PostMapping("queryPage")
    public ApiResponse queryPage(@RequestBody AuditFlowItemSearchModel searchModel){
        return auditFlowItemService.queryPage(searchModel);
    }

    @GetMapping("restart")
    public ApiResponse queryPage(int businessId,String sceneCode){
        return auditFlowItemService.restart(businessId,sceneCode);
    }
}
