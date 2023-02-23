package com.wf.flow.engine.flowfactory.holder.impl;


import com.wf.flow.engine.flowfactory.IFlowFactory;
import com.wf.flow.engine.flowfactory.holder.FlowFactoryBeanHolderService;
import com.wf.flow.engine.groovy.GroovyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/1 10:54
 */

@Slf4j
@Service
public class FlowFactoryBeanHolderServiceImpl implements FlowFactoryBeanHolderService {

    @Autowired(required = false)
    private List<IFlowFactory> flowFactoryList;


    @Autowired
    private GroovyService groovyService;

    private Map<String, IFlowFactory> flowFactoryServiceMap = new HashMap<>();

    @PostConstruct
    private void init() {
        if(!CollectionUtils.isEmpty(flowFactoryList)) {
            flowFactoryList.forEach(flowFactory -> flowFactoryServiceMap.put(flowFactory.getScene(),flowFactory));
            log.info("Process load flow factory end size：{}",flowFactoryServiceMap.size());
        }

        //从数据库查询脚本 todo 可以从脚本加载对应的流程
        String scriptCode ="";
        String script = "";

//        flowFactoryServiceMap.put(scriptCode, new IFlowFactory() {
//            @Override
//            public String getScene() {
//                return scriptCode;
//            }
//
//            @Override
//            public FlowService initFlow(String customId) {
//                Map<String, Object> params = new HashMap();
//                params.put("customId",customId);
//                try {
//                    Object result = groovyService.execute(script, params);
//                    if(result instanceof FlowService){
//                        return (FlowService)result;
//                    }
//                    return null;
//                }catch (Exception e){
//                    log.warn("init flow from groovy script fail customId {} ",customId,e);
//                }
//                return null;
//            }
//        });
    }

    @Override
    public IFlowFactory getFlow(String sceneCode) {
        if(!flowFactoryServiceMap.containsKey(sceneCode)){
            return null;
        }
        return flowFactoryServiceMap.get(sceneCode);
    }

}
