package com.wf.flow.engine.cache.loader;

import com.wf.flow.engine.cache.CacheLoader;
import com.wf.flow.engine.cache.ICache;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wenfeng.zhu
 * @description 固定 EXPIRY 时间没有使用的就过期 在put的时候会触发删除过期没有使用的key
 * @date 2022/8/9 17:21
 */
@Slf4j
public abstract class AbstractCycleRefreshCache<C,K,V> implements ICache<C,K,V>, CacheLoader<C> {

    private Map<K, Long> expiryRecordMap = new ConcurrentHashMap<>();

    /**
     * 如果get的时候重新定义过期时间则设置成true
     */
    private boolean resetExpireWhenGet = false;

    /**
     * 过期时间 毫秒
     */
    private long expiry = 1000*30;

    public void setExpiry(long expiryTime) {
        this.expiry = expiryTime;
    }

    public void setResetExpireWhenGet(boolean resetExpireWhenGet) {
        this.resetExpireWhenGet = resetExpireWhenGet;
    }

    protected abstract void putEntry(K key, V value);
    protected abstract V removeEntry(K key);
    protected abstract V getEntry(K key);

    @Override
    public V get(K key) {
        if(resetExpireWhenGet){
            expiryRecordMap.computeIfPresent(key,(k,v)->calcExpiryTime());
        }
        return getEntry(key);
    }

    @Override
    public void put(K key, V value) {
        removeUselessKey();
        expiryRecordMap.put(key,calcExpiryTime());
        putEntry(key,value);
    }

    @Override
    public V remove(K key) {
        expiryRecordMap.remove(key);
        return removeEntry(key);
    }

    /**
     * 固定周期到期了重新加载
     */
    @Override
    public V get(K key, Callable<? extends V> loader) {
        V value;
        checkAndExpiryKey(key);
        if(Objects.isNull(value = get(key))){
            try {
                value = loader.call();
                if(Objects.nonNull(value)){
                    put(key,value);
                }
            } catch (Exception e) {
                log.info("get cache load value err",e);
            }
        }
        return value;
    }

    /**
     * 固定周期到期了重新加载
     */
    @Override
    public V computeIfAbsent(C content){
        V value;
        K key = loadKey(content);
        checkAndExpiryKey(key);
        if(Objects.isNull(value = get(key))){
            value = loadValue(content);
            if(Objects.nonNull(value)) {
                put(loadKey(content), value);
            }
        }
        return value;
    }

    @Override
    public K loadKey(C o) {
        return null;
    }

    @Override
    public V loadValue(C o) {
        return null;
    }

    private void removeUselessKey() {
        long time = System.currentTimeMillis();
        for (Map.Entry<K, Long> entry : expiryRecordMap.entrySet()) {
            //过期key
            if(entry.getValue() < time){
                remove(entry.getKey());
            }
        }
    }

    private void checkAndExpiryKey(K key) {
        if(!expiryRecordMap.containsKey(key)){
            return;
        }
        long time = System.currentTimeMillis();
        //获取待刷新时间 或者返回当前时间 防止removeUselessKey方法移除key 并发导致取出null值
        Long lastTime = expiryRecordMap.get(key);
        if( Objects.nonNull(lastTime) && (lastTime < time)){
            remove(key);
        }
    }

    private Long calcExpiryTime() {
        return System.currentTimeMillis()+expiry;
    }
}
