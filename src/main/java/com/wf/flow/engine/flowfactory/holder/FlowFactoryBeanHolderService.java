package com.wf.flow.engine.flowfactory.holder;


import com.wf.flow.engine.flowfactory.IFlowFactory;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/1 10:54
 */
public interface FlowFactoryBeanHolderService {

    IFlowFactory getFlow(String sceneCode);
}
