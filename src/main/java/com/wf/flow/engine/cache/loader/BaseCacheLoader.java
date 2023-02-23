package com.wf.flow.engine.cache.loader;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/11 13:22
 */
public class BaseCacheLoader<C,K,V> extends AbstractCycleRefreshCache<C,K,V>{

    private Map<K,V> cacheMap =new ConcurrentHashMap<>();

    @Override
    public String getType() {
        return null;
    }

    @Override
    protected void putEntry(K key, V value) {
        cacheMap.put(key,value);
    }

    @Override
    protected V removeEntry(K key) {
        return cacheMap.remove(key);
    }

    @Override
    protected V getEntry(K key) {
        return cacheMap.get(key);
    }
}
