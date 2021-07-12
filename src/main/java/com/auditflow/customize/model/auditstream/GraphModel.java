package com.auditflow.customize.model.auditstream;

import lombok.Data;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description G6流程图的模型 包含节点和边
 * @date 2020/9/16 15:37
 */

@Data
public class GraphModel {
    List<GNode> nodes;
    List<GEdge> edges;
}
