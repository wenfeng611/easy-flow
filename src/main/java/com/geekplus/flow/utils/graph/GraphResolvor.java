package com.geekplus.flow.utils.graph;

import com.alibaba.fastjson.JSON;
import com.geekplus.flow.enums.FlowEnums;
import com.geekplus.flow.exception.FlowException;
import com.geekplus.flow.model.GEdge;
import com.geekplus.flow.model.GNode;
import com.geekplus.flow.model.GraphModel;
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
    public static Pair<GNode,GNode> getFirstStartNode(String graphJson){
        GraphModel graphModel = JSON.parseObject(graphJson, GraphModel.class);
        List<GNode> nodes = graphModel.getNodes();
        List<GEdge> edges = graphModel.getEdges();

        //条件节点一定存在
        GNode startNode =
                nodes.stream().filter(node-> FlowEnums.FlowNodeTypeEnums.START.getCode().equals(node.getNodeType())).findFirst().orElse(null);

        if(Objects.isNull(startNode)){
            throw new FlowException("没有找到开始节点");
        }
        String g6ConditionNodeID = startNode.getId();

        GEdge finalEdge = edges.stream().filter(edge -> g6ConditionNodeID.equals(edge.getSourceId())).findFirst().get();

        //根据边的 targetID找到节点
        return Pair.with(startNode,nodes.stream().
                filter(node -> finalEdge.getTargetId().equals(node.getId()))
                .findFirst()
                .orElse(new GNode(FlowEnums.FlowNodeTypeEnums.END.getCode())));
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
        if(finalEdge == null) return new GNode(FlowEnums.FlowNodeTypeEnums.END.getCode());
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
        long count = nodes.stream().filter(node-> FlowEnums.FlowNodeTypeEnums.START.getCode().equals(node.getNodeType())).count();
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
                nodes.stream().filter(node-> FlowEnums.FlowNodeTypeEnums.START.getCode().equals(node.getNodeType())).findFirst().orElse(null);
        //结束节点一定存在
        GNode endNode =
                nodes.stream().filter(node-> FlowEnums.FlowNodeTypeEnums.END.getCode().equals(node.getNodeType())).findFirst().orElse(null);
        if(Objects.isNull(conditionNode)){
            return Pair.with(false,"请设置开始节点");
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
     *  根据流程图重的id查询出对应t_flow_node表中的id
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
