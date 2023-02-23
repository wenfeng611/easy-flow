package com.wf.flow.utils;

import com.wf.flow.context.FlowContext;
import org.slf4j.Logger;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/19 10:55
 */
public class ProcessLogUtil {

    /**
     * 日志统一打印模板
     * @param log
     * @param context
     * @param content
     * @param params
     */
    public static void logInfo(Logger log, FlowContext context, String content, Object... params){
        log.info(logFormat(content),buildParams(context,params));
    }

    public static void logWarn(Logger log, FlowContext context, String content, Object... params){
        log.warn(logFormat(content),buildParams(context,params));
    }

    public static void logError(Logger log, FlowContext context, String content, Object... params){
        log.error(logFormat(content),buildParams(context,params));
    }

    private static String logFormat(String content) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{} 节点: {} ").append(content);
        return stringBuilder.toString();
    }

    private static Object[] buildParams(FlowContext context, Object[] params) {
        Object[] paramsFinal = new Object[params.length+2];
        paramsFinal[0] = context.getCustomId();
        paramsFinal[1] = context.getCurrentNode()==null?"开始节点":context.getCurrentNode().getLabel();
        for (int i = 0; i < params.length; i++) {
            paramsFinal[i+2] = params[i];
        }
        return paramsFinal;
    }
    
}
