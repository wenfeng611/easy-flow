package com.wf.flow.engine.ruleParse.parser.impl;


import com.wf.flow.engine.ruleParse.parser.RuleParser;
import com.wf.flow.utils.JSONUtil;

import java.lang.reflect.ParameterizedType;


/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 11:14
 */
public abstract class AbstractRuleParser<T> implements RuleParser<T> {

    public static final String CONFIG="config";


    @Override
    public String getExecuteType() {
        return null;
    }

    @Override
    public boolean parseAndExecute(String ruleDepiction, Object... params) {
        T configRule= JSONUtil.jsonToObj(ruleDepiction, CONFIG,(Class<T>) ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        return judgeRule(configRule, params);
    }

    protected abstract boolean judgeRule(T configRule, Object[] params);
}
