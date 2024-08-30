package com.suchtool.nicecommon.core.exception;

import lombok.Getter;

/**
 * 自定义编码异常
 */
@Getter
public class CustomCodeException extends RuntimeException{
    private Integer code;

    public CustomCodeException() {
        super();
    }

    public CustomCodeException(Integer code) {
        this.code = code;
    }

    public CustomCodeException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CustomCodeException(Throwable cause) {
        super(cause);
    }

    public CustomCodeException(String message) {
        super(message);
    }

    public CustomCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
