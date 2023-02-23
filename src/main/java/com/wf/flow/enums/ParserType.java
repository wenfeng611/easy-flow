package com.wf.flow.enums;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 11:26
 */
public enum  ParserType {

    DEFAULT("default");

    String name;

    public String getName() {
        return name;
    }

    ParserType(String name) {
        this.name = name;
    }
}
