package com.geekplus.flow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wenfeng.zhu
 * @description  场景下面的字段表 scene_field
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowSceneFieldEntity {

    private Integer id;
    private String fieldDescription;  //字段描述
    private String fieldName;  //字段名称
    private String sceneCode;  //关联场景id
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String createrName;        //创建人姓名
    private Integer isValid;  // 0 前台不展示 1 前后台都展示
}
