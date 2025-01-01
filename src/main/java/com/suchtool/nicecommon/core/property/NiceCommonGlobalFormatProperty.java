package com.suchtool.nicecommon.core.property;

import com.suchtool.nicecommon.core.constant.DateTimeFormatTypeEnum;
import lombok.Data;

@Data
public class NiceCommonGlobalFormatProperty {
    /**
     * 启用数字转字符串
     */
    private Boolean enableNumberToString = true;

    /**
     * 日期时间格式类型
     */
    private DateTimeFormatTypeEnum dateTimeFormatType = DateTimeFormatTypeEnum.PRETTY;
}
