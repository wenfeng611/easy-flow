package com.wf.flow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wenfeng.zhu
 * @description  流程配置
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowConfigEntity {
    private Integer id;
    private String flowName;
    private Integer status;     //FlowEnums.FlowStatusEnums 0未启用  1启用  2禁用
    private String graphJson;   //G6图生成的json
    private String sceneCode;   //流程需要关联场景

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String createrName;        //创建人姓名
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;


    private String conditionNodeId; //条件节点的G6id
    private String sceneName;

}