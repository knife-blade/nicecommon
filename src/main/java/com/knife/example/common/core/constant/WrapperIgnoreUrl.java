package com.knife.example.common.constant;

import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局响应中不包装的url列表
 */
public interface WrapperIgnoreUrl {
    List<String> KNIFE4J = Arrays.asList(
            "/doc.html",
            "/swagger-resources",
            "/swagger-resources/configuration",
            "/v3/api-docs",
            "/v2/api-docs",
            "/webjars/**");

    List<String> ALL = new ArrayList<>(KNIFE4J);

    static boolean isInWrapperIgnoreUrl(String uri) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String s : WrapperIgnoreUrl.ALL) {
            if (pathMatcher.match(s, uri)) {
                return true;
            }
        }
        return false;
    }
}
