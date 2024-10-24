package com.suchtool.nicecommon.core.property;

import lombok.Data;

@Data
public class NiceCommonAdviceProperty {
    /**
     * 启用全局异常处理
     */
    private Boolean enableGlobalExceptionAdvice = true;

    /**
     * 启用全局异常日志打印
     */
    private Boolean enableGlobalExceptionAdviceLog = true;

    /**
     * 启用全局响应处理
     */
    private Boolean enableGlobalResponseAdvice = true;

    /**
     * 全局异常处理切面的顺序
     */
    private Integer globalExceptionAdvice = 20000;

    /**
     * 全局响应切面的顺序
     */
    private Integer globalResponseAdvice = 25000;
}
