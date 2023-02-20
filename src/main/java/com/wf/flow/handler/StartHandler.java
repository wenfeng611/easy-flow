package com.wf.flow.handler;

import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.handler.context.FlowContext;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import com.wf.flow.utils.graph.GraphResolvor;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 9:40
 */

@Slf4j
@Component
public class StartHandler extends AbstractNodeStateHandler {

    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.START.getCode();
    }

    @Override
    public FlowResult handle(FlowContext context) {
        String username = context.getUsername();
        String sceneCode = context.getSceneCode();
        Integer businessId = context.getBusinessId();
        log.info("businessId {} 流程开始。",businessId);
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
        List<FlowConfigEntity> flowEntitys = flowMapper.findBySceneCode(context.getSceneCode());

        if(CollectionUtils.isEmpty(flowEntitys)){
            //update
            log.info("businessId: {} 没有找到流程 更新item 状态为 2未找到流程",businessId);
            flowItemMapper.updateFlowItemStatusAndNodeId(FlowEnums.FlowItemStatusEnums.NOTFOUND.getCode(),null,businessId);
            detailMapper.save(initDetail(businessId));

            return FlowResult.fail(FLOW_NOT_FOUNT,businessId, FlowEnums.FlowItemStatusEnums.NOTFOUND.getCode());
        }



        FlowConfigEntity flow = flowEntitys.get(0);
        //更新item流程id
        flowItemMapper.updateFlowId(businessId,flow.getId());
        //记录结束
        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setStatus(FlowEnums.FlowItemStatusEnums.STARTING.getCode());
        flowResult.setBusinessId(context.getBusinessId());
        flowItemMapper.updateFlowItemStatusAndNodeId(FlowEnums.FlowItemStatusEnums.STARTING.getCode(),"",context.getBusinessId());
        detailMapper.save(FlowItemDetailEntity.buildDefault(context.getCurrentNode(),null,context.getBusinessId(), "开始"));

        //找到开始节点
        Pair<GNode, GNode> gNodePair = GraphResolvor.getFirstStartNode(flow.getGraphJson());

        GNode gNode = gNodePair.getValue1();
        NodeState nextState = chooseNextHandler(gNode);

        context.setFlow(flow);
        context.setCurrentNode(gNode);
        context.setState(nextState);
        return context.getState().handle(context);
    }

    @Override
    protected void handleBusiness(FlowContext context) { }

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