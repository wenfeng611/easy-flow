package com.auditflow.customize.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wenfeng.zhu
 * @description 审核规则配置表 audit_rule
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditRuleEntity {
    private Integer id;
    private boolean enableCondition;          //启用条件审核
    private Integer statisfyType;    //0 全部满足  1任意
    private String successToNodeId; //成功流转节点
    private String failToNode;      //失败流转节点
    private String autoJudgeRule;   // 多个条件组成的json数组
    private Integer flowId;          //流程id
    private String g6nodeId;        //G6生成的id
    private Integer nodeType;        //节点类型  1申请条件、2自定义、3结束、4驳回 AuditStreamEnums.FlowNodeTypeEnums

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String createrName;        //创建人姓名
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

}
