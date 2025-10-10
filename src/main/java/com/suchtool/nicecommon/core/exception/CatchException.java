package com.suchtool.nicecommon.core.exception;

/**
 * 捕获异常
 */
public class CatchException extends RuntimeException{
    public CatchException(Throwable cause) {
        super(cause);
    }

    public CatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
