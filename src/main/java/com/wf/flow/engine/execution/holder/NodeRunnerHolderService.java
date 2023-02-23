package com.wf.flow.engine.execution.holder;


import com.wf.flow.engine.execution.runner.NodeRunner;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/2 10:36
 */
public interface NodeRunnerHolderService {

    NodeRunner get(String type);
}
