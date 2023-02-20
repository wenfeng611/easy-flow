package com.wf.flow.handler.holder;

import com.wf.flow.handler.NodeState;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 18:52
 */
public interface StateHandlerHolderService {

    NodeState getStateHandler(String type);
}
