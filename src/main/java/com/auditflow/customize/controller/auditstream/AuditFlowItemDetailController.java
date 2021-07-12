package com.auditflow.customize.controller.auditstream;

import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditFlowItemSearchModel;
import com.auditflow.customize.service.auditstream.AuditFlowItemDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("audit/flowitemdetail")
public class AuditFlowItemDetailController {

    @Autowired
    private AuditFlowItemDetailService auditFlowItemDetailService;

    /**
     *  根据item的id查询审核的细节
     * @param searchModel  flowItemId 和分页信息
     */
    @PostMapping("queryPage")
    public ApiResponse queryPage(@RequestBody AuditFlowItemSearchModel searchModel){
        return auditFlowItemDetailService.queryPage(searchModel);
    }
}
