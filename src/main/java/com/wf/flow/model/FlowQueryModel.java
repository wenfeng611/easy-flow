package com.wf.flow.model;

import lombok.Data;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/15 10:05
 */

@Data
public class FlowQueryModel {

    private String name;  //名称
    private int pagenum;    //pagenum
    private int pagesize;   //pagesize
    private String sceneCode; //场景代码
    private int flowStatus;   //流程页面搜索状态

}
