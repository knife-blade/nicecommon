package com.suchtool.nicecommon.core.constant;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(10000, "访问成功"),

    BUSINESS_FAIL(20001, "业务异常"),
    SYSTEM_FAIL(20005, "系统异常"),
    ;

    private final int code;
    private final String description;

    ResultCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
