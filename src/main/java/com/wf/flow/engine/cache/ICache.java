package com.wf.flow.engine.cache;


import java.util.concurrent.Callable;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/9 15:17
 */
public interface ICache<C,K,V> {

    String getType();

    V get(K key, Callable<? extends V> loader);

    V get(K key);

    void put(K key, V value);

    V remove(K key);

    V computeIfAbsent(C content);

}
