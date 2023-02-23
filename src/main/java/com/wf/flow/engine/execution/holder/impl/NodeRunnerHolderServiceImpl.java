package com.wf.flow.engine.execution.holder.impl;


import com.wf.flow.engine.execution.holder.NodeRunnerHolderService;
import com.wf.flow.engine.execution.runner.NodeRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/2 10:37
 */

@Service
@Slf4j
public class NodeRunnerHolderServiceImpl implements NodeRunnerHolderService {

    @Autowired(required = false)
    List<NodeRunner> nodeRunnerList;

    private Map<String, NodeRunner> nodeRunnerMap = new HashMap<>();

    @PostConstruct
    private void init() {
        nodeRunnerList.forEach(nodeRunner -> nodeRunnerMap.put(nodeRunner.getType(),nodeRunner));
        log.info("Process load node Runner end size：{}",nodeRunnerMap.size());
    }
    
    @Override
    public NodeRunner get(String type) {
        if(!nodeRunnerMap.containsKey(type)){
            log.warn("找不到type:{}对应的节点处理引擎",type);
            return null;
        }
        return nodeRunnerMap.get(type);
    }
    
}
