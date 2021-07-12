package com.auditflow.customize.model.auditstream;

import lombok.Data;

/**
 * @author wenfeng.zhu
 * @description 流程返回统一结果
 * @date 2020/9/16 19:00
 */

@Data
public class FlowResult {

    private boolean success;   //是否成功  没有找到流程会失败 但是会记录下流程 以后可以在列表重新发起
    private String failReason; //失败原因

    private Integer businessId;     //流程item的id 业务需要记录的id

    private Integer status;        //流程状态 AuditStreamEnums.FlowAuditStatusEnums 3审核中（等待人工时是该状态） 4已完成  5驳回  6终止（只有业务人工审核之后要终止才会有）

    private String auditUserids;    //审核人的id ,隔开

    public static FlowResult fail(String failReason,Integer businessId,Integer status){
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(false);
        flowResult.setFailReason(failReason);
        flowResult.setBusinessId(businessId);
        flowResult.setStatus(status);
        return flowResult;
    }
}
