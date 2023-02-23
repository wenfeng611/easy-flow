package com.wf.flow.engine.cache;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/9 17:08
 */
public interface CacheLoader<C> {

    Object loadKey(C c);
    Object loadValue(C c);
}
