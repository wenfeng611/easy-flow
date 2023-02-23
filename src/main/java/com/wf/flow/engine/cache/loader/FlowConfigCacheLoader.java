package com.wf.flow.engine.cache.loader;

import com.wf.flow.enums.CacheType;
import com.wf.flow.entity.FlowConfigEntity;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wenfeng.zhu
 * @description 默认30s 查询数据库更新一次
 * @date 2022/8/11 13:17
 */

@Component
public class FlowConfigCacheLoader extends BaseCacheLoader<FlowConfigEntity, String, FlowConfigEntity> {

    @PostConstruct
    public void init(){
        //1分钟过期
        super.setExpiry(5*60*1000L);
        super.setResetExpireWhenGet(false);
    }

    @Override
    public String getType() {
        return CacheType.FLOW_CONFIG.name();
    }

    @Override
    public String loadKey(FlowConfigEntity flowConfigEntity) {
        return String.valueOf(flowConfigEntity.getId());
    }

    @Override
    public FlowConfigEntity loadValue(FlowConfigEntity flowConfigEntity) {
        return flowConfigEntity;
    }
}
