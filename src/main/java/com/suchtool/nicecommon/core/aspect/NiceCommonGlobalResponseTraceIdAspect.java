package com.suchtool.nicecommon.core.aspect;

import com.suchtool.nicecommon.core.constant.ProcessIgnoreUrl;
import com.suchtool.nicecommon.core.property.NiceCommonGlobalResponseTraceIdProperty;
import com.suchtool.nicelog.util.NiceLogTraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 响应的header中添加traceId
 */
@Slf4j
@Aspect
public class NiceCommonGlobalResponseTraceIdAspect implements Ordered {
    private final NiceCommonGlobalResponseTraceIdProperty responseTraceIdProperty;

    public NiceCommonGlobalResponseTraceIdAspect(NiceCommonGlobalResponseTraceIdProperty responseTraceIdProperty) {
        this.responseTraceIdProperty = responseTraceIdProperty;
    }

    @Override
    public int getOrder() {
        return responseTraceIdProperty.getOrder();
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut() {
    }

    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        doProcess();
    }

    @AfterThrowing(value = "pointcut()", throwing = "throwingValue")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
        doProcess();
    }

    private void doProcess() {
        try {
            ServletRequestAttributes servletRequestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes == null) {
                log.warn("ServletRequestAttributes为null，不处理");
                return;
            }

            HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();
            if (httpServletResponse == null) {
                log.warn("HttpServletResponse为null，不处理");
                return;
            }

            HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
            if (httpServletRequest != null) {
                String requestURI = httpServletRequest.getRequestURI();
                if (isIgnoreUrl(requestURI)) {
                    return;
                }
            }

            if (responseTraceIdProperty.getEnable() != null
                    && responseTraceIdProperty.getEnable()) {
                String headerName = responseTraceIdProperty.getHeaderName();
                if (StringUtils.hasText(headerName)) {
                    String traceId = NiceLogTraceIdUtil.readTraceId();
                    if (StringUtils.hasText(traceId)) {
                        httpServletResponse.addHeader(headerName, traceId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("响应头添加Trace-Id失败");
        }
    }

    private boolean isIgnoreUrl(String uri) {
        if (!CollectionUtils.isEmpty(responseTraceIdProperty.getIgnoreUrl())) {
            AntPathMatcher pathMatcher = new AntPathMatcher();
            for (String path : responseTraceIdProperty.getIgnoreUrl()) {
                if (pathMatcher.match(path, uri)) {
                    return true;
                }
            }
        }

        return false;
    }
}