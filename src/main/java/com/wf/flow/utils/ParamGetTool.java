package com.wf.flow.utils;

import com.wf.flow.context.FlowContext;

import java.util.Map;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 13:34
 */
public class ParamGetTool {

    /**
     * 优先从上下文参数中取字段value
     * @param fieldName
     * @param context
     * @return
     */
    public static Object getFieldValueFromContextFirst(String fieldName, FlowContext context){
        Map<String, Object> paramsMap = context.getParams();
        if(Objects.nonNull(paramsMap) && paramsMap.containsKey(fieldName)){
            return paramsMap.get(fieldName);
        }

        return context.getFieldStrategy().geFieldByName(context.getCustomId(),fieldName,context.getBusinessData());
    }
}
