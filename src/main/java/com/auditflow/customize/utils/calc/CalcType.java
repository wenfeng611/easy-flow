package com.auditflow.customize.utils.calc;

public enum CalcType {

    greater(">"), less("<"), equal("="),greaterEqual(">="), lessEqual("<="),notEquals("!=");

    private String text;

    CalcType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static CalcType get(String text) {
        CalcType res = null;
        for (CalcType value : CalcType.values()) {
            if (value.text.equals(text)) {
                res = value;
                break;
            }
        }
        return res;
    }
}
