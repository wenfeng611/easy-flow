package com.wf.flow.engine.groovy;

import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/9 14:56
 */
public interface GroovyService {

    Object execute(String scriptContent);

    Object execute(String scriptContent, Map<String, Object> params);
}
