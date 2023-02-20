package com.wf.flow.utils;

import org.springframework.context.ApplicationContext;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/17 9:24
 */
public class SpringContextUtils {

    private static ApplicationContext applicationContext;

    public static void setContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return (T)getApplicationContext().getBean(clazz);
    }
}
