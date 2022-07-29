package com.geekplus.flow.service.strage;


import com.geekplus.flow.model.FlowResult;

/**
 * @author wenfeng.zhu
 * @description  获取参数的统一接口  需要业务实现
 * @date 2020/9/17 9:08
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
     * @param businessId 业务id
     * @param fieldName  字段名称
     * @return
     */
    default Object geFieldByName(Integer businessId, String fieldName,Object data){
        return null;
    };

    /**
     *  流程 run方法之后的回调
     * @param flowResult 流程返回的结果
     */
    default void callAfterRun(FlowResult flowResult){};

    /**
     *  流程 continue方法之后的回调
     * @param flowResult 流程返回的结果
     */
    default void callAfterContinue(FlowResult flowResult, Object businessData){};

    /**
     *  流程 restart方法之后的回调
     * @param flowResult 流程返回的结果
     */
    default void callAfterRestart(FlowResult flowResult){};

}
