package com.auditflow.customize.enums;

/**
 * @author wenfeng.zhu
 * @description  审核流通用枚举
 */
public  interface AuditStreamEnums {

    //场景code枚举 跟业务对应上
    enum SenceCodeEnums {
        MATERIAL("material","素材审核");
        private String senceCode;
        private String description;

        SenceCodeEnums(String senceCode, String description) {
            this.senceCode = senceCode;
            this.description = description;
        }

        public String getSenceCode() {
            return senceCode;
        }
    }

    //节点类型枚举
    enum NodeTypeEnums {
        SUPERIAL(1,"直属上级"),
        DESIGNATED_PERSON(2,"指定人员"),
        BUSINESSMANAGER(3,"商务经理"),
        PRODUCTMANAGER(4,"产品总监");

        private int code;
        private String description;

        NodeTypeEnums(int code, String description) {
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
            for (NodeTypeEnums value : NodeTypeEnums.values()) {
                if(value.getCode() == code){
                    res=value.getDescription();
                    break;
                }
            }
            return res;
        }
    }

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
        CONDITION(1,"申请条件"),
        CUSTOMER(2,"自定义"),
        END(3,"结束"),
        ABORD(4,"驳回"),
        TERMINAL(5,"终止");

        private int code;
        private String description;


        FlowNodeTypeEnums(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static FlowNodeTypeEnums get(int text) {
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

    enum FlowAuditStatusEnums{
        WAITING(1,"初始状态"),
        NOTFOUND(2,"未找到流程"),
        RUNNING(3,"审核中"),
        END(4,"已完成"),
        ABORD(5,"驳回"),
        TERMINAL(6,"终止");

        private int code;
        private String description;


        FlowAuditStatusEnums(int code, String description) {
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
            for (FlowAuditStatusEnums value : FlowAuditStatusEnums.values()) {
                if(value.getCode() == code){
                    res=value.getDescription();
                    break;
                }
            }
            return res;
        }
    }
}
