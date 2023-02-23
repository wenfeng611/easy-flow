package com.wf.flow.engine.node.handler.impl;

import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.cache.service.CacheService;
import com.wf.flow.engine.execution.holder.NodeRunnerHolderService;
import com.wf.flow.engine.graph.GraphResolverService;
import com.wf.flow.engine.node.handler.NodeState;
import com.wf.flow.engine.retry.ProcessErrorHandler;
import com.wf.flow.engine.retry.RetryTask;
import com.wf.flow.utils.HolderSelectorUtil;
import com.wf.flow.utils.ProcessLogUtil;
import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.mapper.FlowConfigMapper;
import com.wf.flow.mapper.FlowItemDetailMapper;
import com.wf.flow.mapper.FlowItemMapper;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import com.wf.flow.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 */

@Slf4j
public abstract class AbstractNodeStateHandler implements NodeState {

    public static final String FLOW_NOT_FOUNT = "未找到流程";

    @Resource
    protected FlowConfigMapper flowMapper;

    @Resource
    protected FlowItemMapper flowItemMapper;

    @Resource
    protected FlowItemDetailMapper detailMapper;

    @Autowired
    protected NodeRunnerHolderService nodeRunnerHolderService;

    @Autowired
    protected GraphResolverService graphResolver;

    @Autowired
    protected CacheService cacheService;

    //根据node找到下一个处理的handler
    public NodeState chooseNextHandler(String nodeType){
        //根据节点类型找到下一个处理的节点
        return HolderSelectorUtil.getNodeState(nodeType);
    }

    @Override
    public FlowResult handle(FlowContext context) {
        FlowNodeEntity flowNodeEntity;

        //开始节点特殊处理
        String type;
        if(context.getState() instanceof StartHandler){
            type = FlowEnums.FlowNodeTypeEnums.START.getCode();
        }else{
            type = context.getCurrentNode().getNodeType();
        }
        flowNodeEntity = cacheService.getNode(type);

        FlowResult result = nodeRunnerHolderService.get(flowNodeEntity.getFunctionType()).execute(context,flowNodeEntity);
        if(Objects.nonNull(result) && !result.isSuccess()){
            logWarn(log,context," 流程处理异常！ {}",result.getFailReason());
            //默认进行异常重试
            ProcessErrorHandler.addRetryTask(new RetryTask(Objects.isNull(context.getParentNodeId())?context.getCurrentNode().getId():context.getParentNodeId(),context.getMainSceneCode(),context.getCustomId()));
            return result;
        }
        //执行逻辑都自己实现了 不需要往下执行
        if(flowNodeEntity.getExecuteType() == 0){
            return result;
        }

        //上个节点传过来的参数保存进context
        if(Objects.nonNull(result) && Objects.nonNull(result.getOutPutParams()) && result.getOutPutParams().size()>0){
            if(Objects.isNull(context.getParams())){
                context.setParams(new HashMap<>());
            }
            Map<String, Object> uniqueKeyMap = new HashMap<>();
            //参数加上节点的唯一g6id 保证参数具体到节点 防止参数重复覆盖
            result.getOutPutParams().forEach((key, value) -> uniqueKeyMap.put(context.getCurrentNode().getId() + "-" + key, value));

            context.getParams().putAll(uniqueKeyMap);
        }

        String nodeId = context.getCurrentNode().getId();
        GNode firstNextNodeBYG6NodeId = graphResolver.getFirstNextNodeBYG6NodeId(context, nodeId);

        //更新context里面的值
        flowItemMapper.updateFlowItemStatusAndNodeIdWithParams(context.getCurrentNode().getNodeType(),FlowEnums.FlowItemStatusEnums.RUNNING.getCode(),nodeId,context.getBusinessId(), JSONUtil.objToJson(context.getParams()));
        detailMapper.save(FlowItemDetailEntity.buildDefault(context.getCurrentNode(),firstNextNodeBYG6NodeId,context.getBusinessId(),""));

        NodeState nextState = chooseNextHandler(firstNextNodeBYG6NodeId.getNodeType());

        context.setParentNodeId(nodeId);
        context.setCurrentNode(firstNextNodeBYG6NodeId);
        context.setState(nextState);
        return context.getState().handle(context);
    }

    //返回值标识是否需要往下找节点继续执行
    protected abstract FlowResult handleBusiness(FlowContext context, Map<String, Object> params);


    public void logInfo(Logger log, FlowContext context, String content, Object... params){
        ProcessLogUtil.logInfo(log,context,content,params);
    }

    public void logWarn(Logger log, FlowContext context, String content, Object... params){
        ProcessLogUtil.logWarn(log,context,content,params);
    }

    public void logError(Logger log, FlowContext context, String content, Object... params){
        ProcessLogUtil.logError(log,context,content,params);
    }

}
