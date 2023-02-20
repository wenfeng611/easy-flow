package com.wf.flow.exception;

/**
 * @author wenfeng.zhu
 * @description  异常
 */


public class FlowException extends RuntimeException  {
    public FlowException(){
        super();
    }

    public FlowException(String message){
        super(message);
    }

    public FlowException(String message, Throwable cause){
        super(message,cause);
    }

    public FlowException(Throwable cause) {
        super(cause);
    }
}
