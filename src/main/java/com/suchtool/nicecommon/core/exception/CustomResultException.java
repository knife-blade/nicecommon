package com.suchtool.nicecommon.core.exception;

import com.suchtool.nicecommon.core.model.ResultWrapper;
import lombok.Getter;

/**
 * 自定义返回值异常
 */
@Getter
public class CustomResultException extends RuntimeException{
    private ResultWrapper<?> result;

    public CustomResultException() {
        super();
    }

    public CustomResultException(ResultWrapper<?> result) {
        super(result.getMessage());
        this.result = result;
    }

    public CustomResultException(ResultWrapper<?> result, Throwable cause) {
        super(result.getMessage(), cause);
        this.result = result;
    }
}
