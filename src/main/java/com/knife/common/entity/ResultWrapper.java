package com.knife.common.entity;

import com.knife.common.constant.ResultCode;
import lombok.Data;

@Data
public class ResultWrapper<T> {
    private boolean success;

    private Integer code;

    private String message;

    private T data;

    private ResultWrapper() {
    }

    public static <T> ResultWrapper<T> success() {
        return restResult(null, ResultCode.SUCCESS.getCode(), null);
    }

    public static <T> ResultWrapper<T> success(T data) {
        return restResult(data, ResultCode.SUCCESS.getCode(), null);
    }

    public static <T> ResultWrapper<T> success(T data, String msg) {
        return restResult(data, ResultCode.SUCCESS.getCode(), msg);
    }

    public static <T> ResultWrapper<T> failure() {
        return restResult(null, ResultCode.SYSTEM_FAILURE.getCode(), null);
    }

    public static <T> ResultWrapper<T> failure(String msg) {
        return restResult(null, ResultCode.SYSTEM_FAILURE.getCode(), msg);
    }

    public static <T> ResultWrapper<T> failure(T data) {
        return restResult(data, ResultCode.SYSTEM_FAILURE.getCode(), null);
    }

    public static <T> ResultWrapper<T> failure(T data, String msg) {
        return restResult(data, ResultCode.SYSTEM_FAILURE.getCode(), msg);
    }

    public static <T> ResultWrapper<T> failure(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> ResultWrapper<T> restResult(T data, int code, String message) {
        ResultWrapper<T> resultWrapper = new ResultWrapper<>();
        resultWrapper.setCode(code);
        resultWrapper.setData(data);
        resultWrapper.setMessage(message);
        resultWrapper.setSuccess(code == ResultCode.SUCCESS.getCode());

        return resultWrapper;
    }
}