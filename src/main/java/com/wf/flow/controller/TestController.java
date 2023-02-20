package com.wf.flow.controller;

import com.wf.flow.model.FlowResult;
import com.wf.flow.service.Flow;
import com.wf.flow.service.strage.IFlowHandleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 10:33
 */

@Slf4j
@RestController
@RequestMapping("test/flow")
public class TestController {

     public Integer bussId;

    @RequestMapping("create")
    public FlowResult testCreateFlow(boolean isMain, String orderType, boolean mark) {


        Flow flow = Flow.initFlow("test", "pickingConplete", new IFlowHandleStrategy() {

            @Override
            public void callSaveBusinessId(Integer businessId, Object businessData) {
                bussId = businessId;
                log.info("业务保存流程id：{}",businessId);
            }

            @Override
            public Object geFieldByName(Integer businessId, String fieldName,Object data) {
                log.info("获取字段：{}  业务数据：{}",fieldName,data);
                switch (fieldName){
                    case "isMain":
                        return isMain;
                    case "orderType":
                        return orderType;
                    case "mark":
                        return mark;

                }
                return null;
            }
        });
        //启动流程
        FlowResult result = flow.runFlow("container1");

        return result;
    }


    @RequestMapping("continue")
    public Object continueFlow(Integer itemid,boolean isMain,String orderType,boolean mark) {
        Flow flow = Flow.initFlow("test", "pickingCompleteNextStep", new IFlowHandleStrategy() {

            @Override
            public Object geFieldByName(Integer businessId, String fieldName,Object data) {
                log.info("获取字段：{}  业务数据：{}",fieldName,data);
                switch (fieldName){
                    case "isMain":
                        return isMain;
                    case "orderType":
                        return orderType;
                    case "mark":
                        return mark;

                }
                return null;
            }
        });
        return flow.continueFlow(itemid,"",false,"container1");
    }
}
