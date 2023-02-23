package com.wf.flow.engine.ruleParse.parser.impl;


import com.wf.flow.context.FlowContext;
import com.wf.flow.enums.ParserType;
import com.wf.flow.utils.ParamGetTool;
import com.wf.flow.model.simple.ConditionRule;
import com.wf.flow.model.simple.RuleItem;
import com.wf.flow.utils.calc.CalcTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 11:13
 */

@Slf4j
@Component
public class DefaultSimpleParser extends AbstractRuleParser<ConditionRule> {

    @Override
    public String getExecuteType() {
        return ParserType.DEFAULT.getName();
    }

    @Override
    protected boolean judgeRule(ConditionRule rule, Object[] params) {
        FlowContext context = (FlowContext)params[0];

        Integer statisfyType =  rule.getStatisfyType();
        List<RuleItem> ruleItems = rule.getRuleItems();

        boolean allflag = true;
        boolean oneflag = false;

        //遍历规则 从fieldStrategy中取值
        for (RuleItem ruleItem : ruleItems) {
            Object firstValue = ParamGetTool.getFieldValueFromContextFirst(ruleItem.getField(),context);
            Object secondValue = "1".equals(ruleItem.getType())? ParamGetTool.getFieldValueFromContextFirst(ruleItem.getTarget(),context):ruleItem.getTarget();
            boolean statisy = false;
            if(!Objects.isNull(firstValue) && !Objects.isNull(secondValue) && StringUtils.isNotBlank(ruleItem.getTarget())){
                statisy = CalcTool.calc(firstValue,secondValue,ruleItem.getSymbol());
            }
            if((!(allflag = allflag && statisy) && statisfyType==0)  || ((oneflag = oneflag || statisy) && statisfyType==1)){
                break;
            }
        }
        return statisfyType==0?allflag:oneflag;
    }

}