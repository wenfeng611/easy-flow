package com.wf.flow.engine.strategy;


/**
 * @author wenfeng.zhu
 * @description  获取参数的统一接口  需要业务实现
 */
public interface IFlowHandleStrategy {

    /**
     *  业务保存流程系统返回的业务id
     * @param businessId  流程系统生成的id
     * @param businessData 业务传的值 用于找到对应业务的数据来保存业务id
     */
    default void callSaveBusinessId(Integer businessId, Object businessData) {};

    /**
     *  流程系统根据字段名称获取值
     * @param customId 业务id
     * @param fieldName  字段名称
     * @return
     */
    default Object geFieldByName(String customId, String fieldName, Object data){
        return null;
    };

}
