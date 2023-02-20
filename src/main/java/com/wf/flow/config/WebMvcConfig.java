package com.wf.flow.config;

import com.wf.flow.interceptor.CrossInterceptorHandler;
import com.wf.flow.interceptor.GlobalInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2021/7/5 10:41
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CrossInterceptorHandler()).addPathPatterns("/**");
        registry.addInterceptor(new GlobalInterceptor()).addPathPatterns("/**");
    }
}
