package com.suchtool.nicecommon.core.exception;

/**
 * 系统异常
 */
public class SystemException extends RuntimeException{
    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
