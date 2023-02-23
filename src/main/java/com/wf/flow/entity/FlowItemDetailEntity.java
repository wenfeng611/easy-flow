package com.wf.flow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wf.flow.model.GNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wenfeng.zhu
 * @description   每条流程具体的信息
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowItemDetailEntity {

    private Integer id;
    private Integer flowItemId;        //流程的itemId
    private String operateUsername;    //操作人
    private String operateInfo;        //操作记录
    private String currentG6NodeId;    //当前G6node的id

    private String currentG6NodeLabel;    //当前G6node的name

    private String nextG6NodeId;    //下一个G6node的id

    private String nextG6NodeLabel; //下一个当前G6node的name

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    public static FlowItemDetailEntity buildDefault(GNode current, GNode next, Integer businessId, String operateInfo){
        return FlowItemDetailEntity.builder()
                .flowItemId(businessId)
                .operateUsername("System")
                .operateInfo(operateInfo)
                .currentG6NodeId(current!=null?current.getId():"")
                .currentG6NodeLabel(current!=null?current.getLabel():"")
                .nextG6NodeId(next!=null?next.getId():"")
                .nextG6NodeLabel(next!=null?next.getLabel():"")
                .createTime(new Date())
                .build();
    }
}