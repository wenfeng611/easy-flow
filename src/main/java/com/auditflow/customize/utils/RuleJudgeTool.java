package com.auditflow.customize.utils;

import com.alibaba.fastjson.JSON;
import com.auditflow.customize.model.auditstream.RuleItem;
import com.auditflow.customize.service.auditstream.strage.IFlowHandleStrategy;
import com.auditflow.customize.utils.calc.CalcTool;
import io.micrometer.core.instrument.util.StringUtils;
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
     * @param statisfyType   0全部满足 1满足任意
     * @param businessId     业务id 即流程item的id
     * @return
     */
    public static  boolean judgeFlowWithRule(String autoJudgeRule, IFlowHandleStrategy fieldStrategy, Integer statisfyType, Integer businessId) {
        List<RuleItem> ruleItems = JSON.parseArray(autoJudgeRule).toJavaList(RuleItem.class);
        boolean allflag = true;
        boolean oneflag = false;

        //遍历规则 从fieldStrategy中取值
        for (RuleItem ruleItem : ruleItems) {
            Object firstValue = fieldStrategy.geFieldByName(businessId,ruleItem.getField());
            Object secondValue = "1".equals(ruleItem.getType())?fieldStrategy.geFieldByName(businessId,ruleItem.getTarget()):ruleItem.getTarget();
            boolean statisy = false;
            if(!Objects.isNull(firstValue) && !Objects.isNull(secondValue) && StringUtils.isNotBlank(ruleItem.getTarget())){
                statisy = CalcTool.calc(firstValue,secondValue,ruleItem.getSymbol());
            }
            if((!(allflag = allflag && statisy) && statisfyType==0)  || ((oneflag = oneflag || statisy) && statisfyType==1)){
                break;
            }
        }
        log.info("businessId: {} 判断规则 结果为： {}",businessId,statisfyType==0?allflag:oneflag);
        return statisfyType==0?allflag:oneflag;
    }
}
