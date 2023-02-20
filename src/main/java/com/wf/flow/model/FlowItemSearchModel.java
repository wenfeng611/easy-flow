package com.wf.flow.model;

import lombok.Data;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/23 18:07
 */

@Data
public class FlowItemSearchModel {

    private Integer flowId;
    private String sceneCode;

    private Integer flowStatus;

    private Integer pagenum;
    private Integer pagesize;

    private Integer flowItemId;
    private Integer businessId;   //业务id
}
