package com.geekplus.flow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wenfeng.zhu
 * @description
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowItemEntity {

    private Integer id;
    private Integer flowId;        //流程的id
    private String currentNodeId;  //当前节点id G6中的
    private Integer flowStatus;    //状态 FlowEnums.FlowItemStatusEnums

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    private String createrName;        //创建人姓名

    private String sceneCode;


    private String flowName;
    private String formUrl;

    private Integer oldFlowItemId;
}
