package com.wf.flow.engine.flowfactory;


import com.wf.flow.engine.service.FlowService;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/1 10:47
 */
public interface IFlowFactory {

    String getScene();

    FlowService initFlow(String customId);
}
