package com.wf.flow.engine.node.handler.impl;


import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.anno.FlowNode;
import com.wf.flow.engine.notify.AutoNotifyTask;
import com.wf.flow.engine.notify.AutoNotifyTaskHandler;
import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/12/6 14:17
 */

@Slf4j
@Component
@FlowNode(nodeName = "lang.wms.fed.autoNotify",executeType = 0)
public class AutoNotifyFixDelayNode extends AbstractNodeStateHandler{

    @Override
    protected FlowResult handleBusiness(FlowContext context, Map<String, Object> params) {
        logInfo(log,context,"自动唤醒：{}",context.getCurrentNodeDisplayName());
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setBusinessId(context.getBusinessId());

        int fixDelay = 60; //默认60s
        Map<String, String> nodeConfig = context.getNodeConfigEntityOrElseNull(Map.class);
        if (Objects.nonNull(nodeConfig)) {
            fixDelay = Integer.parseInt(nodeConfig.get("delaySecond"));
        }
        AutoNotifyTaskHandler.addSchedulerTask(new AutoNotifyTask(context.getCurrentNode().getId(),context.getMainSceneCode(),context.getCustomId(),fixDelay));

        GNode node = context.getCurrentNode();
        flowItemMapper.updateFlowItemStatusAndNodeIdWithParams(getType(), FlowEnums.FlowItemStatusEnums.RUNNING.getCode(),node.getId(),context.getBusinessId(),null);
        detailMapper.save(FlowItemDetailEntity.buildDefault(context.getCurrentNode(),null,context.getBusinessId(), ""));


        return flowResult;
    }

    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.AUTO_NOTIFY.getCode();
    }
}
