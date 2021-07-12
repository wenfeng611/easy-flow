package com.auditflow.customize.service.auditstream.strage;

import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.model.auditstream.FlowResult;

/**
 * @author wenfeng.zhu
 * @description  默认重启之后走run之后的回调
 */
public abstract class AbstractFlowHandlerStrategy implements IFlowHandleStrategy {

    @Override
    public void callAfterRestart(FlowResult flowResult) {
        callAfterRun(flowResult);
    }

    @Override
    public String getAuditUsersByNodeType(Integer type,String auditUserids,Integer businessId){
        //指定人员的话直接返回
        if(AuditStreamEnums.NodeTypeEnums.DESIGNATED_PERSON.getCode() == type){
            return auditUserids;
        }else {
            return getAuditUsersCustom(type,businessId);
        }
    }

    protected abstract String getAuditUsersCustom(Integer type,Integer businessId);
}
