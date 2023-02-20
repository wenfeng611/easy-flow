package com.wf.flow.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenfeng.zhu
 * @description  g6节点
 * @date 2020/9/16 15:39
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GNode {
    private String nodeType;   //节点类型
    private Integer nodeId;     //t_flow_node表中的id  0的话就是不是自定义节点
    private String id;          //G6生成的id
    private String label;       //G6生成的节点的label

    public GNode(String nodeType) {
        this.nodeType = nodeType;
    }
}
