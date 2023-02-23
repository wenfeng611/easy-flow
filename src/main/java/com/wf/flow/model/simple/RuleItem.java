package com.wf.flow.model.simple;

import lombok.Data;

/**
 * @author wenfeng.zhu
 * @description  规则详情
 */
@Data
public class RuleItem {

    private String field;   //字段
    private String symbol;  //符号
    private String type;   //类型  0值或者1字段
    private String target;  //目标值或者字段名
}
