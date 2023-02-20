package com.wf.flow.enums;

/**
 * @author wenfeng.zhu
 * @description  通用枚举
 */
public  interface FlowEnums {

    //流程图状态的枚举  0未启用  1启用  2禁用
    enum FlowStatusEnums {
        NOTENABLE(0,"未启用"),
        ENABLE(1,"启用中"),
        FORBIDDEN(2,"已禁用");

        private int code;
        private String description;


        FlowStatusEnums(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    enum FlowNodeTypeEnums{
        START("start","开始"),
        CONDITION("judge","判断"),
        END("end","结束"),
        TERMINAL("terminal","终止"),
        WAITING("waiting","等待");

        private String code;
        private String description;


        FlowNodeTypeEnums(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static FlowNodeTypeEnums get(String text) {
            FlowNodeTypeEnums res = null;
            for (FlowNodeTypeEnums value : FlowNodeTypeEnums.values()) {
                if (value.getCode() == text) {
                    res = value;
                    break;
                }
            }
            return res;
        }
    }

    enum FlowItemStatusEnums{
        STARTING(0,"开启流程"),
        INIT(1,"初始状态"),
        NOTFOUND(2,"未找到流程"),
        RUNNING(3,"运行中"),
        END(4,"已完成"),
        TERMINAL(6,"终止"),
        CONFIG_ERROR(7,"配置异常");

        private int code;
        private String description;


        FlowItemStatusEnums(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static String from(int code){
            String res = "未知";
            for (FlowItemStatusEnums value : FlowItemStatusEnums.values()) {
                if(value.getCode() == code){
                    res=value.getDescription();
                    break;
                }
            }
            return res;
        }
    }
}
