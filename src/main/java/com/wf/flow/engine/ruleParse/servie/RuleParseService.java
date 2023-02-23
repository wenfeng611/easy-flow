package com.wf.flow.engine.ruleParse.servie;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 11:05
 */
public  interface RuleParseService {

    boolean execute(String ruleDepiction, Object... params);
}
