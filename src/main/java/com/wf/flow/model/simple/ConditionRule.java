package com.wf.flow.model.simple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionRule {
    private Integer statisfyType;    //0全部满足 1满足任意
    List<RuleItem> ruleItems;
}
