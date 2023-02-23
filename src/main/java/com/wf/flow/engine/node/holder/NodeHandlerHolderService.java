package com.wf.flow.engine.node.holder;


import com.wf.flow.engine.node.handler.NodeState;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 18:52
 */
public interface NodeHandlerHolderService {

    NodeState getStateHandler(String type);
}
