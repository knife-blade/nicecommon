package com.suchtool.nicecommon.core.constant;

import lombok.Getter;

@Getter
public enum DateTimeFormatTypeEnum {
    TIMESTAMP("时间戳"),
    PRETTY("美化(年月日时分秒)"),
    NONE("不处理")
    ;

    private final String description;

    DateTimeFormatTypeEnum(String description) {
        this.description = description;
    }
}
