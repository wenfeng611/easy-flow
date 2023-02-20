package com.wf.flow.handler.holder.impl;

import com.wf.flow.handler.NodeState;
import com.wf.flow.handler.holder.StateHandlerHolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 18:56
 */

@Slf4j
@Service
public class StateHandlerHolderServiceImpl implements StateHandlerHolderService, ApplicationListener<ApplicationEvent> {

    @Autowired(required = false)
    List<NodeState> nodeStateHandlerList;

    private Map<String, NodeState> noteStateServiceMap = new HashMap<>();

    @Override
    public NodeState getStateHandler(String type) {
        if(!noteStateServiceMap.containsKey(type)){
            log.warn("找不到type:{}对应的处理器",type);
            return null;
        }
        return noteStateServiceMap.get(type);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //只加载一次
        if(noteStateServiceMap.keySet().size() == 0){
            log.info("加载流程处理节点service 开始");
            nodeStateHandlerList.forEach(nodeState -> noteStateServiceMap.put(nodeState.getType(),nodeState));
            log.info("加载流程处理节点service 结束 size：{}",nodeStateHandlerList.size());
        }
    }
}
