package com.suchtool.nicecommon.core.constant;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(1000, "访问成功"),

    AUTHENTICATION_ABOUT_TO_EXPIRE(1005, "认证即将过期"),

    BUSINESS_FAIL(2000, "业务异常"),
    SYSTEM_FAIL(2005, "系统异常"),
    ;

    private final int code;
    private final String description;

    ResultCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
