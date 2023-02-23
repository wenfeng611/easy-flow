package com.wf.flow.engine.node.handler.impl;


import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.anno.FlowNode;
import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 9:40
 */

@Slf4j
@Order(1)
@Component
@FlowNode(nodeName = "lang.wms.fed.startNode")
public class StartHandler extends AbstractNodeStateHandler {

    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.START.getCode();
    }

    @Override
    protected FlowResult handleBusiness(FlowContext context, Map<String, Object> params) {
        String username = context.getUsername();
        String sceneCode = context.getSceneCode();
        Integer businessId = context.getBusinessId();

        logInfo(log,context,"流程开始");
        //如果第一次没找到流程 重新启动流程的时候会带着businessId
        if(context.getBusinessId() == null || context.getBusinessId() == 0){
            //需要新建流程
            FlowItemEntity flowItemEntity = initFlowItem(username,sceneCode);
            //保存 flowItemEntity  其id就是业务需要保存的id
            flowItemMapper.save(flowItemEntity);
            //设置businessId
            context.setBusinessId(flowItemEntity.getId());
        }

        //查找开始节点
        FlowConfigEntity flow = cacheService.getFlowConfig(context.getSceneCode());

        if(Objects.isNull((flow))){
            //update
            logInfo(log,context,"没有找到流程");
            flowItemMapper.updateFlowItemStatusAndNodeIdWithParams(getType(),FlowEnums.FlowItemStatusEnums.NOTFOUND.getCode(),null,businessId,null);
            detailMapper.save(initDetail(businessId));

            return FlowResult.fail(FLOW_NOT_FOUNT,businessId);
        }

        //更新item流程id
        flowItemMapper.updateFlowId(businessId,flow.getId());

        //找到开始节点
        GNode gNode = graphResolver.getFirstStartNode(flow.getGraphJson());
        context.setFlow(flow);
        context.setCurrentNode(gNode);

        return FlowResult.ok(businessId);
    }

    private FlowItemDetailEntity initDetail(Integer businessId) {
        return FlowItemDetailEntity.builder()
                .flowItemId(businessId)
                .operateInfo(FLOW_NOT_FOUNT)
                .createTime(new Date())
                .build();
    }

    private FlowItemEntity initFlowItem(String username, String sceneCode) {
        return FlowItemEntity.builder()
                .flowId(0)
                .currentNodeId("")
                .sceneCode(sceneCode)
                .createrName(username)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
    }

}