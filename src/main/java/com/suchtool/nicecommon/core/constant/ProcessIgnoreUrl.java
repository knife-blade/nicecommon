package com.suchtool.nicecommon.core.constant;

import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 不处理的url列表
 */
public interface ProcessIgnoreUrl {
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
        for (String path : ProcessIgnoreUrl.ALL) {
            if (pathMatcher.match(path, uri)) {
                return true;
            }
        }
        return false;
    }
}
