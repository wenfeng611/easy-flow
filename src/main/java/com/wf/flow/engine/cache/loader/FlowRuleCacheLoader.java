package com.wf.flow.engine.cache.loader;


import com.wf.flow.enums.CacheType;
import com.wf.flow.entity.FlowRuleEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/11 13:45
 */

@Component
public class FlowRuleCacheLoader extends BaseCacheLoader<FlowRuleEntity, String, FlowRuleEntity> {

    @PostConstruct
    public void init(){
        //1分钟过期
        super.setExpiry(60*1000L);
        super.setResetExpireWhenGet(false);
    }

    @Override
    public String getType() {
        return CacheType.RULE.name();
    }


    @Override
    public String loadKey(FlowRuleEntity flowNodeEntity) {
        return flowNodeEntity.getFlowId() + flowNodeEntity.getG6nodeId();
    }

    @Override
    public FlowRuleEntity loadValue(FlowRuleEntity flowNodeEntity) {
        return flowNodeEntity;
    }
}
