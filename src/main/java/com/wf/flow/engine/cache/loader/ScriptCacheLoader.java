package com.wf.flow.engine.cache.loader;

import com.wf.flow.enums.CacheType;
import com.wf.flow.utils.GroovyUtil;
import groovy.lang.GroovyObject;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/9 15:21
 */

@Component
public class ScriptCacheLoader extends BaseCacheLoader<String, String, GroovyObject> {

    @PostConstruct
    public void init(){
        //60分钟过期
        super.setExpiry(12*60*60*1000L);
        super.setResetExpireWhenGet(true);
    }

    @Override
    public String getType() {
        return CacheType.SCRIPT.name();
    }

    @Override
    public String loadKey(String scriptContent) {
        return DigestUtils.md5DigestAsHex(scriptContent.getBytes());
    }

    @Override
    public GroovyObject loadValue(String scriptContent) {
        return GroovyUtil.compile(scriptContent);
    }

}