package com.wf.flow.engine.ruleParse.parser;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 12:13
 */
public interface ParamSupply<T> {
    Object supply(T t);
}
