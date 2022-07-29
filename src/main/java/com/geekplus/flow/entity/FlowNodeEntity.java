package com.geekplus.flow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wenfeng.zhu
 * @description  节点表
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowNodeEntity {
    private Integer id;
    private String nodeName;
    private String type;            //FlowEnums.NodeTypeEnums

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String createrName;        //创建人姓名
    private Integer isValid;  // 0 前台不展示 1 前后台都展示
}
