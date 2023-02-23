package com.wf.flow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wenfeng.zhu
 * @description  节点表
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowNodeEntity {
    private Integer id;
    private String nodeName;
    private String nodeDes;
    private String type;            //FlowEnums.NodeTypeEnums

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String createrName;        //创建人姓名
    private Integer isValid;  // 0 前台不展示 1 前后台都展示

    private String functionType;
    private String scriptContent;

    private Integer executeType;
    private String outputParams; //节点输出参数列表
    private String inputParams; //节点输入参数列表
    private String groupName;   //分组名称
}
