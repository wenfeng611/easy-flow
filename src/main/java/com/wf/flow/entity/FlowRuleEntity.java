package com.wf.flow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wenfeng.zhu
 * @description 规则配置表 t_flow_rule
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowRuleEntity {
    private Integer id;
    private String autoJudgeRule;   // 多个条件组成的json数组
    private Integer flowId;          //流程id
    private String g6nodeId;        //G6生成的id
    private String nodeType;        //节点类型   FlowEnums.FlowNodeTypeEnums

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String createrName;        //创建人姓名
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

}
