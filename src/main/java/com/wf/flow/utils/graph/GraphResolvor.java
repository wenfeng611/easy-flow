package com.wf.flow.utils.graph;

import com.alibaba.fastjson.JSON;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.model.GEdge;
import com.wf.flow.model.GNode;
import com.wf.flow.model.GraphModel;
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
