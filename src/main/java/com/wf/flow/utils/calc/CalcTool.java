package com.wf.flow.utils.calc;

import java.math.BigDecimal;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/17 13:28
 */

public class CalcTool {

    /**
     *  比较值 根据 firstValue区分类型 int  double  String只能equals和notequals
     *
     * @param firstValue  第一个值 根据其确定类型
     * @param secondValue 需要比较的值
     * @param symbol      符号
     * @return
     */
    public static boolean calc(Object firstValue, Object secondValue, String symbol) {
        CalcType calcType = CalcType.get(symbol);

        if(firstValue instanceof String){
            return calcString(firstValue,secondValue,calcType);
        }

        if(firstValue instanceof Boolean){
            return calcBoolean(firstValue,secondValue,calcType);
        }
        
        if(firstValue instanceof Double || firstValue instanceof Float || firstValue instanceof Integer || firstValue instanceof Long || firstValue instanceof BigDecimal){
            return calcOtherSymbols(firstValue,secondValue,calcType);
        }

        return false;
    }

    private static boolean calcBoolean(Object firstValue, Object secondValue, CalcType calcType) {
        boolean first = (Boolean) firstValue;
        boolean second = Boolean.parseBoolean(String.valueOf(secondValue));

        switch (calcType) {
            case equal:
                return first == second;
            case notEquals:
                return first != second;
        }
        return false;
    }

    private static boolean calcString(Object firstValue, Object secondValue, CalcType calcType) {
        switch (calcType) {
            case equal:
                return calcEqual(firstValue,secondValue);
            case notEquals:
                return !calcEqual(firstValue,secondValue);
        }
        return false;
    }

    private static boolean calcOtherSymbols(Object firstValue, Object secondValue, CalcType calcType) {
        switch (calcType) {
            case equal:
                return calcEqual(firstValue,secondValue);
            case notEquals:
                return !calcEqual(firstValue,secondValue);
            case greater:
                return calcGreater(firstValue,secondValue);
            case less:
               return !calcGreater(firstValue,secondValue) && !calcEqual(firstValue,secondValue);
            case greaterEqual:
                return calcGreater(firstValue,secondValue) || calcEqual(firstValue,secondValue);
            case lessEqual:
                return !calcGreater(firstValue,secondValue);
        }
        return false;
    }

    private static boolean calcGreater(Object firstValue, Object secondValue) {
        if(firstValue instanceof Double){
            Double first = (Double) firstValue;
            Double second  = Double.parseDouble(String.valueOf(secondValue));
            return first > second;
        }
        if(firstValue instanceof Float){
            Float first = (Float) firstValue;
            Float second  = Float.parseFloat(String.valueOf(secondValue));
            return first > second;
        }

        if(firstValue instanceof Integer){
            Integer first = (Integer)firstValue;
            Integer second  = Integer.parseInt(String.valueOf(secondValue));
            return first > second;
        }
        if(firstValue instanceof Long){
            Long first = (Long) firstValue;
            Long second  = Long.parseLong(String.valueOf(secondValue));
            return first > second;
        }

        if(firstValue instanceof  BigDecimal){
            BigDecimal first = (BigDecimal) firstValue;
            BigDecimal second  = new BigDecimal(String.valueOf(secondValue));
            return first.compareTo(second) == 1;
        }
        return false;
    }

    private static boolean calcEqual(Object firstValue, Object secondValue) {
        String secondStringValue =String.valueOf(secondValue);
        if(firstValue instanceof Double){
            Double first = (Double) firstValue;
            Double second  = Double.parseDouble(secondStringValue);
            return first.equals(second);
        }
        if(firstValue instanceof Float){
            Float first = (Float) firstValue;
            Float second  = Float.parseFloat(secondStringValue);
            return first.equals(second);
        }

        if(firstValue instanceof Integer){
            Integer first = (Integer)firstValue;
            Integer second  = Integer.parseInt(secondStringValue);
            return first.equals(second);
        }
        if(firstValue instanceof Long){
            Long first = (Long) firstValue;
            Long second  = Long.parseLong(secondStringValue);
            return first.equals(second);
        }

        if(firstValue instanceof  BigDecimal){
            BigDecimal first = (BigDecimal) firstValue;
            BigDecimal second  = new BigDecimal(secondStringValue);
            return first.compareTo(second) == 0;
        }
        if(firstValue instanceof  String){
            String first = (String) firstValue;
            return first.equals(secondStringValue);
        }
        return false;
    }

}
