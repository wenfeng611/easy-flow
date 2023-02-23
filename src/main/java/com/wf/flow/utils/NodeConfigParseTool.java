package com.wf.flow.utils;


import com.wf.flow.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 */

@Slf4j
public class NodeConfigParseTool {

    public static final String CONFIG = "config";

    public static final String MAPPING = "mapping";

    /**
     * 获取节点对应的字段映射关系
     * @param nodeRule
     * @return
     */
    public static Map<String, String> getNodeFieldMappings(String nodeRule) {
        return JSONUtil.jsonToObj(nodeRule,MAPPING, Map.class);
    }

    /**
     * 获取节点对应的字段映射关系
     * @param nodeRule
     * @return
     */
    public static <T> T getNodeConfigEntity(String nodeRule, Class<T> clazz) {
        return JSONUtil.jsonToObj(nodeRule,CONFIG,clazz);
    }

}
