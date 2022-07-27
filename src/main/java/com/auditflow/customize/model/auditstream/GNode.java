package com.auditflow.customize.model.auditstream;


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
    private Integer nodeType;   //节点类型  1申请条件、2自定义、3结束、4驳回 5终止 AuditStreamEnums.FlowNodeTypeEnums
    private Integer nodeId;     //audit_node表中的id  0的话就是不是自定义节点
    private String id;          //G6生成的id
    private String label;       //G6生成的节点的label

    public GNode(Integer nodeType) {
        this.nodeType = nodeType;
    }
}
