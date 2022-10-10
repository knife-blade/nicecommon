package com.knife.common.entity;

import com.knife.common.constant.ResultCode;
import lombok.Data;

@Data
public class ResultWrapper<T> {
    private boolean success;

    private Integer code;

    private T data;

    private String message;

    private ResultWrapper() {
    }

    public static <T> ResultWrapper<T> success() {
        return assemble(null, ResultCode.SUCCESS.getCode(), null);
    }

    public static <T> ResultWrapper<T> failure() {
        return assemble(null, ResultCode.SYSTEM_FAILURE.getCode(), null);
    }

    public ResultWrapper<T> data(T data) {
        this.setData(data);
        return this;
    }

    public ResultWrapper<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResultWrapper<T> code(int code) {
        this.setCode(code);
        return this;
    }

    private static <T> ResultWrapper<T> assemble(T data, int code, String message) {
        ResultWrapper<T> resultWrapper = new ResultWrapper<>();
        resultWrapper.setCode(code);
        resultWrapper.setData(data);
        resultWrapper.setMessage(message);
        resultWrapper.setSuccess(code == ResultCode.SUCCESS.getCode());

        return resultWrapper;
    }
}