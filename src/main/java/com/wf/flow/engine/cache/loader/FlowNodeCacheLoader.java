package com.wf.flow.engine.cache.loader;

import com.wf.flow.enums.CacheType;
import com.wf.flow.entity.FlowNodeEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wenfeng.zhu
 * @description 默认30s 查询数据库更新一次
 * @date 2022/8/11 10:59
 */

@Component
public class FlowNodeCacheLoader extends BaseCacheLoader<FlowNodeEntity, String, FlowNodeEntity> {

    @PostConstruct
    public void init(){
        //1分钟过期
        super.setExpiry(60*1000L);
    }

    @Override
    public String getType() {
        return CacheType.NODE.name();
    }

    @Override
    public String loadKey(FlowNodeEntity flowNodeEntity) {
        return flowNodeEntity.getType();
    }

    @Override
    public FlowNodeEntity loadValue(FlowNodeEntity flowNodeEntity) {
        return flowNodeEntity;
    }
}
