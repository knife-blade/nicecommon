package com.suchtool.nicecommon.core.property;

import lombok.Data;

import java.util.List;

/**
 * 全局响应自动包装
 */
@Data
public class NiceCommonGlobalResponseWrapperProperty {
    /**
     * 启用
     */
    private Boolean enable = true;

    /**
     * 切面的顺序
     */
    private Integer order = 25000;

    /**
     * 忽略处理的URL
     */
    private List<String> ignoreUrl;
}
