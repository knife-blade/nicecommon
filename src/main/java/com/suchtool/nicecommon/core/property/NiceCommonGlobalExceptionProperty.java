package com.suchtool.nicecommon.core.property;

import lombok.Data;

/**
 * 全局异常处理
 */
@Data
public class NiceCommonGlobalExceptionProperty {
    /**
     * 启用
     */
    private Boolean enable = true;

    /**
     * 启用日志打印
     */
    private Boolean enableLog = true;

    /**
     * 切面的顺序
     */
    private Integer adviceOrder = 20000;

    /**
     * 是否用e.printStackTrace()直接打印栈追踪
     */
    private Boolean directPrintStackTrace = false;
}
