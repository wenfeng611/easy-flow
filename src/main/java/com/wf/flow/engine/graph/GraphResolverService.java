package com.wf.flow.engine.graph;

import com.alibaba.fastjson.JSON;
import com.wf.flow.context.FlowContext;
import com.wf.flow.engine.cache.service.CacheService;
import com.wf.flow.engine.ruleParse.servie.RuleParseService;
import com.wf.flow.utils.ProcessLogUtil;
import com.wf.flow.entity.FlowRuleEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.exception.FlowException;
import com.wf.flow.model.GEdge;
import com.wf.flow.model.GNode;
import com.wf.flow.model.GraphModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wenfeng.zhu
 * @description  解析流程图数据
 * @date 2020/9/17 14:38
 */
@Slf4j
@Component
public class GraphResolverService {

    @Autowired
    private RuleParseService ruleParseService;

    @Autowired
    private CacheService cacheService;

    /**
     *  通过开始节点查找下一个node 第一个是条件节点
     * @param graphJson  流程图数据
     * @return
     */
    public GNode getFirstStartNode(String graphJson){
        GraphModel graphModel = JSON.parseObject(graphJson, GraphModel.class);
        List<GNode> nodes = graphModel.getNodes();

        //条件节点一定存在
        GNode startNode =
                nodes.stream().filter(node-> FlowEnums.FlowNodeTypeEnums.START.getCode().equals(node.getNodeType())).findFirst().orElse(null);

        if(Objects.isNull(startNode)){
            throw new FlowException("没有找到开始节点");
        }
        return startNode;
    }

    /**
     * 根据G6nodeid查找GNode
     * @param graphJson  流程图数据
     * @param nodeId     G6中node的id
     * @return
     */
    public GNode getNodeByG6NodeId(String graphJson, String nodeId){
        GraphModel graphModel = JSON.parseObject(graphJson, GraphModel.class);
        List<GNode> nodes = graphModel.getNodes();
        return nodes.stream().filter(node -> nodeId.equals(node.getId())).findFirst().orElse(null);
    }

    //解析nodeId下面的第一个节点
    public GNode getFirstNextNodeBYG6NodeId(FlowContext context, String nodeId){
        String graphJson = context.getFlow().getGraphJson();
        GraphModel graphModel = JSON.parseObject(graphJson, GraphModel.class);
        List<GNode> nodes = graphModel.getNodes();
        List<GEdge> edges = graphModel.getEdges();
        //可能走的边
        List<GEdge> possibleEdges = edges.stream().filter(edge -> nodeId.equals(edge.getSource())).collect(Collectors.toList());
        //如果没找到边 直接默认结束节点
        if(CollectionUtils.isEmpty(possibleEdges)) return new GNode(FlowEnums.FlowNodeTypeEnums.END.getCode());

        //只有一个边直接往下走
        if(possibleEdges.size() == 1) {
            return nodes.stream().filter(node -> node.getId().equals(possibleEdges.get(0).getTarget())).findFirst().get();
        }

        //获取边的条件
        List<String> allEdges = possibleEdges.stream().map(GEdge::getId).collect(Collectors.toList());
        List<FlowRuleEntity> ruleEntities = cacheService.getFlowNodeRules(context.getFlow().getId(), allEdges);
        Map<String, String> rulesMap = ruleEntities.stream().collect(Collectors.toMap(FlowRuleEntity::getG6nodeId,FlowRuleEntity::getAutoJudgeRule));

        String firstTargetEdgeId = null;
        for (Map.Entry<String, String> idRuleEntry : rulesMap.entrySet()) {
            //判断规则
            boolean pair = ruleParseService.execute(idRuleEntry.getValue(),context);
            if(pair){
                firstTargetEdgeId = idRuleEntry.getKey();
                break;
            }
        }
        if(StringUtils.isBlank(firstTargetEdgeId)){
            //检测是否存在没有条件的边
            List<GEdge> noConditionEdge = possibleEdges.stream().filter(e -> !rulesMap.containsKey(e.getId())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(noConditionEdge)){
                ProcessLogUtil.logWarn(log,context,"边条件设置错误,找不到对应的下一个节点请检查！{}",context.getCurrentNodeDisplayName());
                return new GNode(FlowEnums.FlowNodeTypeEnums.END.getCode());
            }
            return nodes.stream().filter(node -> noConditionEdge.get(0).getTarget().equals(node.getId())).findFirst().get();
        }

        //根绝边的 targetID找到节点
        final String finalEdge = firstTargetEdgeId;
        String targetNodeId = edges.stream().filter(edge -> edge.getId().equals(finalEdge)).findFirst().get().getTarget();
        return nodes.stream().filter(node -> targetNodeId.equals(node.getId())).findFirst().get();
    }

}
