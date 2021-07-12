package com.auditflow.customize.model.auditstream;

import lombok.Data;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/23 18:07
 */

@Data
public class AuditFlowItemSearchModel {

    private Integer flowId;
    private Integer auditStatus;
    private String sceneCode;

    private Integer pagenum;
    private Integer pagesize;

    private Integer flowItemId;
    private Integer businessId;   //业务id
}
