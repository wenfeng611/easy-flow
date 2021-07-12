package com.auditflow.customize.utils.graph;

import com.alibaba.fastjson.JSON;
import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.exception.AuditFlowException;
import com.auditflow.customize.model.auditstream.GEdge;
import com.auditflow.customize.model.auditstream.GNode;
import com.auditflow.customize.model.auditstream.GraphModel;
import org.javatuples.Pair;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description  解析流程图数据
 * @date 2020/9/17 14:38
 */
public class GraphResolvor {

    /**
     *  通过条件节点查找下一个node 第一个是条件节点 第二个是下一个节点
     * @param graphJson  流程图数据
     * @return
     */
    public static Pair<GNode,GNode> getFirstNode(String graphJson){
        GraphModel graphModel = JSON.parseObject(graphJson, GraphModel.class);
        List<GNode> nodes = graphModel.getNodes();
        List<GEdge> edges = graphModel.getEdges();

        //条件节点一定存在
        GNode conditionNode =
                nodes.stream().filter(node-> AuditStreamEnums.FlowNodeTypeEnums.CONDITION.getCode() == node.getNodeType()).findFirst().orElse(null);

        if(Objects.isNull(conditionNode)){
            throw new AuditFlowException("没有找到条件节点");
        }
        String g6ConditionNodeID = conditionNode.getId();

        GEdge finalEdge = edges.stream().filter(edge -> g6ConditionNodeID.equals(edge.getSourceId())).findFirst().get();

        //根绝边的 targetID找到节点
        return Pair.with(conditionNode,nodes.stream().
                filter(node -> finalEdge.getTargetId().equals(node.getId()))
                .findFirst()
                .orElse(new GNode(AuditStreamEnums.FlowNodeTypeEnums.END.getCode())));
    }

    /**
     * 根据G6nodeid查找GNode
     * @param graphJson  流程图数据
     * @param nodeId     G6中node的id
     * @return
     */
    public static GNode getNodeByG6NodeId(String graphJson,String nodeId){
        GraphModel graphModel = JSON.parseObject(graphJson, GraphModel.class);
        List<GNode> nodes = graphModel.getNodes();
        return nodes.stream().filter(node -> nodeId.equals(node.getId())).findFirst().orElse(null);
    }

    //解析nodeId下面的第一个节点
    public static GNode getFirstNextNodeBYG6NodeId(String graphJson,String nodeId){
        GraphModel graphModel = JSON.parseObject(graphJson, GraphModel.class);
        List<GNode> nodes = graphModel.getNodes();
        List<GEdge> edges = graphModel.getEdges();

        GEdge finalEdge = edges.stream().filter(edge -> nodeId.equals(edge.getSourceId())).findFirst().orElseGet(null);
        //如果没找到边 直接默认结束节点
        if(finalEdge == null) return new GNode(AuditStreamEnums.FlowNodeTypeEnums.END.getCode());
        //根绝边的 targetID找到节点
        return nodes.stream().filter(node -> finalEdge.getTargetId().equals(node.getId())).findFirst().get();
    }

    /**
     * 判断是否只有一个条件节点
     * @param graphJson 流程图数据
     * @return
     */
    public static boolean hasOnlyOneCondition(String graphJson){
        GraphModel graphModel = JSON.parseObject(graphJson, GraphModel.class);
        List<GNode> nodes = graphModel.getNodes();

        //条件节点一定存在
        long count = nodes.stream().filter(node-> AuditStreamEnums.FlowNodeTypeEnums.CONDITION.getCode() == node.getNodeType()).count();
        return count==1L;
    }

    /**
     * 判断是否存在条件节点、是否存在边  流程图的初步判断  todo：是否存在死循环判断
     * @param graphJson  流程图数据
     * @return
     */
    public static  Pair<Boolean,String> judgeFlowGraph(String graphJson){
        GraphModel graphModel = JSON.parseObject(graphJson, GraphModel.class);
        List<GNode> nodes = graphModel.getNodes();
        List<GEdge> edges = graphModel.getEdges();

        //条件节点一定存在
        GNode conditionNode =
                nodes.stream().filter(node-> AuditStreamEnums.FlowNodeTypeEnums.CONDITION.getCode() == node.getNodeType()).findFirst().orElse(null);
        //结束节点一定存在
        GNode endNode =
                nodes.stream().filter(node-> AuditStreamEnums.FlowNodeTypeEnums.END.getCode() == node.getNodeType()).findFirst().orElse(null);
        if(Objects.isNull(conditionNode)){
            return Pair.with(false,"请设置条件节点");
        }
        if(Objects.isNull(endNode)){
            return Pair.with(false,"请设置结束节点");
        }
        if(CollectionUtils.isEmpty(nodes)){
            return Pair.with(false,"请设置流程图的节点");
        }

        if(CollectionUtils.isEmpty(edges)){
            return Pair.with(false,"请设置流程图的连接线");
        }

        return Pair.with(true,null);
    }

    /**
     *  根据流程图重的id查询出对应xc_audit_node表中的id
     * @param graphJson 流程图数据
     * @param g6nodeId G6流程图中的id
     * @return
     */
    public static Integer getCurrentNodeTypeByG6NodeId(String graphJson,String g6nodeId){
        GraphModel graphModel = JSON.parseObject(graphJson, GraphModel.class);
        List<GNode> nodes = graphModel.getNodes();
        GNode nodeEntity = nodes.stream().filter(node -> node.getId().equals(g6nodeId)).findFirst().orElse(new GNode());
        return nodeEntity.getNodeId();
    }
}
