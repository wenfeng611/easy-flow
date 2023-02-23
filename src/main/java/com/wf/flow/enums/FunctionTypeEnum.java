package com.wf.flow.enums;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 15:54
 */
public enum FunctionTypeEnum {

    GROOVY("groovy"),SPRING_BEAN("spring-bean");

    private String name;

    public String getName() {
        return name;
    }

    FunctionTypeEnum(String name) {
        this.name = name;
    }

    public static FunctionTypeEnum of(String name){
        for (FunctionTypeEnum triggerFunctionTypeEnum : FunctionTypeEnum.values()) {
            if(triggerFunctionTypeEnum.getName().equals(name)){
                return triggerFunctionTypeEnum;
            }
        }
        return null;
    }
}
