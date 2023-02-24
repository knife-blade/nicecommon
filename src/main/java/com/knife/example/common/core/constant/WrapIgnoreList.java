package com.knife.example.common.core.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局响应中不包装的url列表
 */
public interface WrapIgnoreList {
    List<String> KNIFE4J = Arrays.asList(
            "/doc.html",
            "/swagger-resources",
            "/swagger-resources/configuration",
            "/v3/api-docs",
            "/v2/api-docs",
            "/webjars/**");

    List<String> ALL = new ArrayList<>(KNIFE4J);
}
