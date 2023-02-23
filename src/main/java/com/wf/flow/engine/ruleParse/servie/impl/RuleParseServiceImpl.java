package com.wf.flow.engine.ruleParse.servie.impl;


import com.wf.flow.engine.ruleParse.parser.RuleParser;
import com.wf.flow.engine.ruleParse.servie.RuleParseService;
import com.wf.flow.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 11:06
 */

@Slf4j
@Service
public class RuleParseServiceImpl implements RuleParseService {

    public static final String EXECUTE_TYPE="executeType";

    @Autowired(required = false)
    List<RuleParser> ruleParserList;

    private Map<String, RuleParser> ruleParserMap = new HashMap<>();

    @PostConstruct
    private void init() {
        ruleParserList.forEach(ruleParser -> ruleParserMap.put(ruleParser.getExecuteType(),ruleParser));
        log.info("Process load ruleParser end size：{}",ruleParserMap.size());
    }

    @Override
    public boolean execute(String ruleDepiction, Object... params) {
        String executeType = JSONUtil.jsonToObj(ruleDepiction, EXECUTE_TYPE, String.class);
        return ruleParserMap.get(executeType).parseAndExecute(ruleDepiction,params);
    }
}
