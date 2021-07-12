package com.auditflow.customize.entity;

import com.auditflow.customize.model.auditstream.GNode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wenfeng.zhu
 * @description   每条流程具体的审核信息
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditFlowItemDetailEntity {

    private Integer id;
    private Integer flowItemId;        //流程的itemId
    private String auditUsername;      //审核人名称

    private String auditResult;        //审核结果

    private String currentG6NodeId;    //当前G6node的id

    private String currentG6NodeLabel;    //当前G6node的name

    private String nextG6NodeId;    //下一个G6node的id

    private String nextG6NodeLabel; //下一个当前G6node的name

    private Integer auditStatus;    //审核状态

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    public static AuditFlowItemDetailEntity buildDefault(GNode current, GNode next, Integer businessId, Integer auditStatus, String auditResult){
        return AuditFlowItemDetailEntity.builder()
                .flowItemId(businessId)
                .auditUsername("系统")
                .auditResult(auditResult)
                .currentG6NodeId(current!=null?current.getId():"")
                .currentG6NodeLabel(current!=null?current.getLabel():"")
                .nextG6NodeId(next!=null?next.getId():"")
                .nextG6NodeLabel(next!=null?next.getLabel():"")
                .auditStatus(auditStatus)
                .createTime(new Date())
                .build();
    }
}
