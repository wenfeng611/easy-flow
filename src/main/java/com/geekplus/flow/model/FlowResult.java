package com.geekplus.flow.model;

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

    private Integer status;        //流程状态 FlowEnums.FlowItemStatusEnums

    public static FlowResult fail(String failReason,Integer businessId,Integer status){
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(false);
        flowResult.setFailReason(failReason);
        flowResult.setBusinessId(businessId);
        flowResult.setStatus(status);
        return flowResult;
    }
}
