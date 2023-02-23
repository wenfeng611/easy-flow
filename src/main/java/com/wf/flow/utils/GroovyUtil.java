package com.wf.flow.utils;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/9 20:40
 */

@Slf4j
public class GroovyUtil {

    private static GroovyClassLoader groovyClassLoader = new GroovyClassLoader();

    private GroovyUtil() {
    }

    public static GroovyObject compile(String string) {
        GroovyObject groovyObject = null;

        try {
            Class groovyClass = groovyClassLoader.parseClass(string);
            groovyObject = (GroovyObject) groovyClass.newInstance();
        } catch (Exception e) {
            log.warn("new groovy instance exception", e);
            throw new RuntimeException(e);
        }

        return groovyObject;
    }

    public static Object run(GroovyObject groovyObject, String functionName, Object... params) {
        return groovyObject.invokeMethod(functionName, params);
    }
}
