package com.wf.flow.model;

import lombok.Data;

/**
 * @author wenfeng.zhu
 * @description G6流程图中的边
 * @date 2020/9/16 15:39
 */

@Data
public class GEdge {
    private String id;

    private String source;    //起始节点id  是G6生成的id
    private String target;    //目标节点id  是G6生成的id
}
