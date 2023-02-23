package com.wf.flow.engine.cache.manager;


import com.wf.flow.engine.cache.ICache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/9 15:08
 */

@Slf4j
@Service
public class ProcessCacheManager {

    @Autowired(required = false)
    List<ICache> cacheInstances;

    private Map<String, ICache> cacheInstanceMap = new HashMap<>();

    @PostConstruct
    private void init() {
        cacheInstances.forEach(cache -> cacheInstanceMap.put(cache.getType(),cache));
    }

    public Object get(String type, Object key){
        return cacheInstanceMap.get(type).get(key);
    }

    public <T> T get(String type, Object key, Callable loader){
        ICache iCache = cacheInstanceMap.get(type);
        return (T)iCache.get(key,loader);
    }

    public void put(String type, Object key, Object value){
        cacheInstanceMap.get(type).put(key,value);
    }

    public <T> T computeIfAbsent(String type, Object content){
        return (T)cacheInstanceMap.get(type).computeIfAbsent(content);
    }

}
