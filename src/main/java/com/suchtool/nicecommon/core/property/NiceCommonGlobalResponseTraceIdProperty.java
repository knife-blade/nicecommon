package com.suchtool.nicecommon.core.property;

import lombok.Data;

import java.util.List;

/**
 * 全局响应添加TraceId
 */
@Data
public class NiceCommonGlobalResponseTraceIdProperty {
    /**
     * 启用
     */
    private Boolean enable = true;

    /**
     * 切面的顺序
     */
    private Integer order = 21000;

    /**
     * TraceId响应头名称
     */
    private String headerName = "Trace-Id";

    /**
     * 忽略处理的URL
     */
    private List<String> ignoreUrl;
}
