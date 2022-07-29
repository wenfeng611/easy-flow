package com.geekplus.flow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wenfeng.zhu
 * @description  场景 sence
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowSceneEntity {

    private Integer id;
    private String sceneName;  //场景名称
    private String sceneCode;  //FlowEnums.SenceCodeEnums.xxx.getSenceCode
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String createrName;        //创建人姓名
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    private Integer isValid;  // 0 前台不展示 1 前后台都展示
}
