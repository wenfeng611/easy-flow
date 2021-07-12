package com.auditflow.customize.interceptor;

import com.auditflow.customize.context.UserInfoThreadLocalContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2021/7/5 10:38
 */
public class GlobalInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //测试用
        UserInfoThreadLocalContext.init_user();
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserInfoThreadLocalContext.clear_user();
    }
}
