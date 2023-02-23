package com.wf.flow.enums;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/2 10:23
 */
public enum NodeRunnerEnum {

    NODE_HANDLER("nodeHandler"),
    GROOVY("groovy"),
    FLOW_HANDLER("flowHandler"),
    TRIGGER_HANDLER("triggerHandler");

    String value;

    NodeRunnerEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
