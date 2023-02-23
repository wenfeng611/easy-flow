package com.wf.flow.engine.ruleParse.parser;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 11:12
 */
public interface RuleParser<T> {

    String getExecuteType();

    boolean parseAndExecute(String ruleDepiction, Object... params);
}
