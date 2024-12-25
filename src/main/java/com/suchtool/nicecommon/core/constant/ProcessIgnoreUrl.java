package com.suchtool.nicecommon.core.constant;

import com.suchtool.nicetool.util.spring.ApplicationContextHolder;
import com.suchtool.nicetool.util.web.http.url.HttpUrlUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

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
        for (String s : ProcessIgnoreUrl.ALL) {
            String path = s;
            String contextPath = ApplicationContextHolder.getContext().getEnvironment()
                    .getProperty("server.servlet.context-path", "");
            if (StringUtils.hasText(contextPath)) {
                List<String> pathList = Arrays.asList(contextPath, path);
                path = "/" + HttpUrlUtil.joinUrl(pathList, false);
            }
            if (pathMatcher.match(path, uri)) {
                return true;
            }
        }
        return false;
    }
}
