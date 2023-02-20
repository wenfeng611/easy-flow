package com.wf.flow.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/27 16:59
 */

@Component
public class CrossInterceptorHandler extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, accept, authorization, content-type, token, username, from");
        response.setHeader("X-Frame-Options", "SAMEORIGIN");


        return true;
    }

    public void postHandle(HttpServletRequest arg0, HttpServletResponse response, Object arg2,
                           ModelAndView view) throws Exception {
        if (view != null) {
            //view.addObject("models", UKDataContext.model);
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}