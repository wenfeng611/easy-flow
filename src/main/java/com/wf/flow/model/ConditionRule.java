package com.wf.flow.model;

import lombok.Data;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 17:53
 */

@Data
public class ConditionRule {
    private Integer statisfyType;    //0全部满足 1满足任意
    private String successToNodeId;
    private String failToNodeId;
    List<RuleItem> ruleItems;
}
