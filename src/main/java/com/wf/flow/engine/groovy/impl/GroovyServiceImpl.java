package com.wf.flow.engine.groovy.impl;


import com.wf.flow.engine.cache.service.CacheService;
import com.wf.flow.engine.groovy.GroovyService;
import com.wf.flow.utils.GroovyUtil;
import groovy.lang.GroovyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/9 14:57
 */

@Service
public class GroovyServiceImpl implements GroovyService {

    private static final String SCRIPT_ENTRY = "execute";

    @Autowired
    private CacheService cacheService;

    @Override
    public Object execute(String scriptContent) {
        GroovyObject groovyObject = cacheService.getGroovy(scriptContent);
        return GroovyUtil.run(groovyObject, SCRIPT_ENTRY);
    }

    @Override
    public Object execute(String scriptContent, Map<String, Object> params) {
        GroovyObject groovyObject = cacheService.getGroovy(scriptContent);
        return GroovyUtil.run(groovyObject, SCRIPT_ENTRY,params);
    }
}
