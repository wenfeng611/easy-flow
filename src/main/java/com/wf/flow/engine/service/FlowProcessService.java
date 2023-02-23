package com.wf.flow.engine.service;


import com.wf.flow.engine.cache.service.CacheService;
import com.wf.flow.engine.flowfactory.IFlowFactory;
import com.wf.flow.engine.flowfactory.holder.FlowFactoryBeanHolderService;
import com.wf.flow.model.FlowResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/1 16:17
 */

@Service
public class FlowProcessService {

    @Autowired
    private FlowFactoryBeanHolderService flowFactoryBeanHolderService;

    @Autowired
    private CacheService cacheService;
    /**
     * 初始化并且开始一个流程
     * @param sceneCode       场景code
     * @param uniqueId        标记流程的唯一id
     * @param businessData    透传到IFlowHandleStrategy中的数据
     * @param params          字段参数map 优先从该map中取值
     * @return
     */
    public FlowResult initAndStartFlow(String sceneCode, String uniqueId, Object businessData, Map<String, Object> params){
        if(!checkSceneValid(sceneCode)){
            return FlowResult.fail(null,uniqueId);
        }
        IFlowFactory flowFactory = flowFactoryBeanHolderService.getFlow(sceneCode);
        FlowService flow = flowFactory.initFlow(uniqueId);
        return flow.runFlow(uniqueId,businessData,params);
    }

    /**
     *
     * @param sceneCode           场景code
     * @param uniqueId            标记流程的唯一id
     * @param operateInfoRecord   操作记录
     * @param businessData        透传到IFlowHandleStrategy中的数据
     * @param params              字段参数map 优先从该map中取值 流程引擎会记录下开始时候的参数 这里是新增的参数
     * @return
     */
    public FlowResult continueFlow(String sceneCode, String uniqueId, String operateInfoRecord, Object businessData, Map<String, Object> params){
        if(!checkSceneValid(sceneCode)){
            return FlowResult.fail(null,uniqueId);
        }
        IFlowFactory flowFactory = flowFactoryBeanHolderService.getFlow(sceneCode);
        FlowService flow = flowFactory.initFlow(uniqueId);
        return flow.continueFlow(uniqueId,operateInfoRecord,false,businessData,params);
    }

    /**
     *
     * @param sceneCode           场景code
     * @param uniqueId            标记流程的唯一id
     * @param operateInfoRecord   操作记录
     * @param businessData        透传到IFlowHandleStrategy中的数据
     * @param params              字段参数map 优先从该map中取值 流程引擎会记录下开始时候的参数 这里是新增的参数
     * @return
     */
    public FlowResult continueFlow(String sceneCode, String uniqueId, String operateInfoRecord, String eventName, Object businessData, Map<String, Object> params){
        if(!checkSceneValid(sceneCode)){
            return FlowResult.fail(null,uniqueId);
        }
        IFlowFactory flowFactory = flowFactoryBeanHolderService.getFlow(sceneCode);
        FlowService flow = flowFactory.initFlow(uniqueId);
        return flow.continueFlow(uniqueId,operateInfoRecord,false,eventName,businessData,params);
    }

    private boolean checkSceneValid(String sceneCode) {
        return Objects.nonNull(cacheService.getFlowConfig(sceneCode));
    }

}
