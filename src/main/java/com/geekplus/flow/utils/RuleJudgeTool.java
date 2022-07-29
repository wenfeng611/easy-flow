package com.geekplus.flow.utils;

import com.alibaba.fastjson.JSON;
import com.geekplus.flow.model.ConditionRule;
import com.geekplus.flow.model.RuleItem;
import com.geekplus.flow.service.strage.IFlowHandleStrategy;
import com.geekplus.flow.utils.calc.CalcTool;
import io.micrometer.core.instrument.util.StringUtils;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 */

@Slf4j
public class RuleJudgeTool {

    /**
     * 判断规则是否满足
     * @param autoJudgeRule  规则的json
     * @param fieldStrategy  从业务取字段的策略
     * @param businessId     业务id 即流程item的id
     * @param data           业务数据 透传
     * @return
     */
    public static Pair<Boolean,String> judgeFlowWithRule(String autoJudgeRule, IFlowHandleStrategy fieldStrategy, Integer businessId,Object data) {
        ConditionRule rule = JSON.parseObject(autoJudgeRule,ConditionRule.class);
        Integer statisfyType =  rule.getStatisfyType();
        List<RuleItem> ruleItems = rule.getRuleItems();

        boolean allflag = true;
        boolean oneflag = false;

        //遍历规则 从fieldStrategy中取值
        for (RuleItem ruleItem : ruleItems) {
            Object firstValue = fieldStrategy.geFieldByName(businessId,ruleItem.getField(),data);
            Object secondValue = "1".equals(ruleItem.getType())?fieldStrategy.geFieldByName(businessId,ruleItem.getTarget(),data):ruleItem.getTarget();
            boolean statisy = false;
            if(!Objects.isNull(firstValue) && !Objects.isNull(secondValue) && StringUtils.isNotBlank(ruleItem.getTarget())){
                statisy = CalcTool.calc(firstValue,secondValue,ruleItem.getSymbol());
            }
            if((!(allflag = allflag && statisy) && statisfyType==0)  || ((oneflag = oneflag || statisy) && statisfyType==1)){
                break;
            }
        }
        log.info("businessId: {} 判断规则 结果为： {}",businessId,statisfyType==0?allflag:oneflag);
        boolean pass = statisfyType==0?allflag:oneflag;
        return new Pair<>(pass,pass?rule.getSuccessToNodeId():rule.getFailToNodeId());
    }
}
