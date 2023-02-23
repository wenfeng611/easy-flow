package com.wf.flow.engine.node.holder.impl;

import com.wf.flow.engine.anno.FlowNode;
import com.wf.flow.engine.node.handler.NodeState;
import com.wf.flow.engine.node.holder.NodeHandlerHolderService;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.mapper.FlowNodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 18:56
 */

@Slf4j
@Service
public class NodeHandlerHolderServiceImpl implements NodeHandlerHolderService {

    @Autowired(required = false)
    List<NodeState> nodeStateHandlerList;

    @Autowired
    FlowNodeMapper flowNodeMapper;

    private Map<String, NodeState> noteStateServiceMap = new HashMap<>();

    @PostConstruct
    private void init() {
        List<String> allTypes = flowNodeMapper.findAllTypes();
        nodeStateHandlerList.forEach(nodeState -> {
            noteStateServiceMap.put(nodeState.getType(),nodeState);
            if(!allTypes.contains(nodeState.getType())){
                autoInsertNode(nodeState);
            }
        });
        log.info("Process load flow handler end size：{}",nodeStateHandlerList.size());
    }

    @Override
    public NodeState getStateHandler(String type) {
        return noteStateServiceMap.get(StringUtils.isBlank(type)?"default":type);
    }

    private void autoInsertNode(NodeState nodeState) {
        FlowNode flowNode = nodeState.getClass().getDeclaredAnnotation(FlowNode.class);
        if(Objects.isNull(flowNode)){
            log.info("节点 {} 数据未插入数据库 请检查！",nodeState.getType());
            return;
        }
        if(!flowNode.needInsert()){
            return;
        }
        FlowNodeEntity nodeEntity = new FlowNodeEntity();
        nodeEntity.setExecuteType(flowNode.executeType());
        nodeEntity.setType(nodeState.getType());
        nodeEntity.setFunctionType(flowNode.functionType());
        nodeEntity.setCreaterName("System");
        nodeEntity.setCreateTime(new Date());
        nodeEntity.setInputParams(flowNode.inputParams());
        nodeEntity.setOutputParams(flowNode.outputParams());
        nodeEntity.setIsValid(1);
        nodeEntity.setNodeName(flowNode.nodeName());
        nodeEntity.setGroupName(flowNode.groupName());
        nodeEntity.setNodeDes(StringUtils.isBlank(flowNode.nodeDes())?flowNode.nodeName():flowNode.nodeDes());
        flowNodeMapper.save(nodeEntity);
        log.info("自动插入节点 {} 数据进db", nodeEntity.getType());
    }
}