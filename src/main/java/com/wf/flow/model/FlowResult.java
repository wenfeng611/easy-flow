package com.wf.flow.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description 流程返回统一结果
 * @date 2020/9/16 19:00
 */

@Data
public class FlowResult implements Serializable {

    private boolean success = true;   //是否成功  没有找到流程会失败 但是会记录下流程 以后可以在列表重新发起
    private String failReason; //失败原因

    private Object businessId;     //流程item的id

    private Map<String,Object> outPutParams;

    public static FlowResult fail(String failReason, Object businessId){
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(false);
        flowResult.setFailReason(failReason);
        flowResult.setBusinessId(businessId);
        return flowResult;
    }

    public static FlowResult ok(Object businessId){
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setBusinessId(businessId);
        return flowResult;
    }

    public static FlowResult ok(Object businessId,Map<String,Object> outPutParam){
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setBusinessId(businessId);
        flowResult.setOutPutParams(outPutParam);
        return flowResult;
    }
}
