package com.auditflow.customize.factory;

import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.model.auditstream.FlowResult;
import com.auditflow.customize.service.auditstream.AuditFlow;
import com.auditflow.customize.service.auditstream.strage.IFlowHandleStrategy;

/**
 * @author wenfeng.zhu
 * @description  流程工厂
 */
public class FlowFactory {

    /**
     *   创建一个流程
     * @param sceneCode  场景code
     * @param username   创建人
     * @return
     */
    public static AuditFlow createFlow(String sceneCode, String username){
        IFlowHandleStrategy strage = null;
        if(AuditStreamEnums.SenceCodeEnums.MATERIAL.getSenceCode().equals(sceneCode)){
            strage = new IFlowHandleStrategy() {
                @Override
                public void callSaveBusinessId(Integer businessId, Object businessData) {

                }

                @Override
                public Object geFieldByName(Integer businessId, String fieldName) {
                    return null;
                }

                @Override
                public void callAfterRun(FlowResult flowResult) {

                }

                @Override
                public void callAfterContinue(FlowResult flowResult, Object businessData) {

                }

                @Override
                public void callAfterRestart(FlowResult flowResult) {

                }

                @Override
                public String getAuditUsersByNodeType(Integer type, String auditUserids, Integer businessId) {
                    return null;
                }
            };
        }
        return AuditFlow.initFlow(username,sceneCode, strage);
    }

    /**
     *   创建一个流程 带参数
     * @param sceneCode  场景code
     * @param username   创建人
     * @param param      流程需要的参数
     * @return
     */
    public static AuditFlow createFlow(String sceneCode,String username, Object param){
        IFlowHandleStrategy strage = null;
        if(AuditStreamEnums.SenceCodeEnums.MATERIAL.getSenceCode().equals(sceneCode)){
            strage = new IFlowHandleStrategy() {
                @Override
                public void callSaveBusinessId(Integer businessId, Object businessData) {

                }

                @Override
                public Object geFieldByName(Integer businessId, String fieldName) {
                    return null;
                }

                @Override
                public void callAfterRun(FlowResult flowResult) {

                }

                @Override
                public void callAfterContinue(FlowResult flowResult, Object businessData) {

                }

                @Override
                public void callAfterRestart(FlowResult flowResult) {

                }

                @Override
                public String getAuditUsersByNodeType(Integer type, String auditUserids, Integer businessId) {
                    return null;
                }
            };
        }
        return AuditFlow.initFlow(username,sceneCode, strage);
    }
}
