package com.auditflow.customize.exception;

/**
 * @author wenfeng.zhu
 * @description  审核流异常
 */


public class AuditFlowException extends RuntimeException  {
    public AuditFlowException(){
        super();
    }

    public AuditFlowException(String message){
        super(message);
    }

    public AuditFlowException(String message, Throwable cause){
        super(message,cause);
    }

    public AuditFlowException(Throwable cause) {
        super(cause);
    }
}
