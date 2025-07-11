package com.suchtool.nicecommon.core.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suchtool.nicecommon.core.annotation.ResponseWrapperIgnore;
import com.suchtool.nicecommon.core.constant.ProcessIgnoreUrl;
import com.suchtool.nicecommon.core.model.ResultWrapper;
import com.suchtool.nicecommon.core.property.NiceCommonGlobalResponseWrapperProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;

@Slf4j
@ControllerAdvice
public class NiceCommonGlobalResponseWrapperAdvice implements ResponseBodyAdvice<Object>, Ordered {
    private final NiceCommonGlobalResponseWrapperProperty globalResponseProperty;

    public NiceCommonGlobalResponseWrapperAdvice(NiceCommonGlobalResponseWrapperProperty property) {
        this.globalResponseProperty = property;
    }

    @Override
    public int getOrder() {
        return globalResponseProperty.getOrder();
    }

    /**
     * 返回值的含义：是否要处理
     */
    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 若接口返回的类型本身就是ResultWrapper，则无需操作，返回false
        // return !methodParameter.getParameterType().equals(ResultWrapper.class);
        // 本处全部都放过，在后边进行处理
        return true;
    }

    @Override
    @ResponseBody
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        String uri = request.getURI().getPath();
        // 如果不需要处理，直接跳过

        if (isIgnoreUrl(uri)) {
            return body;
        }

        Executable executable = methodParameter.getExecutable();
        if (!(executable instanceof Method)) {
            return body;
        }

        Method method = (Method) executable;
        if (method.isAnnotationPresent(ResponseWrapperIgnore.class)) {
            return body;
        }

        Class<? extends Method> aClass = method.getClass();
        if (aClass.isAnnotationPresent(ResponseWrapperIgnore.class)) {
            return body;
        }

        Class<?> returnType = method.getReturnType();
        if (returnType == String.class) {
            // 若返回值为String类型，需要包装为String类型返回。否则会报错
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ResultWrapper<Object> resultWrapper = ResultWrapper.success().data(body);
                return objectMapper.writeValueAsString(resultWrapper);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("序列化String错误");
            }
        } else if (returnType == ResultWrapper.class) {
            // 如果已经封装过了，不再封装
            return body;
        }
        return ResultWrapper.success().data(body);
    }

    private boolean isIgnoreUrl(String uri) {
        String thisUri = uri;

        if (ProcessIgnoreUrl.isInWrapperIgnoreUrl(thisUri)) {
            return true;
        }

        if (!CollectionUtils.isEmpty(globalResponseProperty.getIgnoreUrl())) {
            AntPathMatcher pathMatcher = new AntPathMatcher();
            for (String path : globalResponseProperty.getIgnoreUrl()) {
                if (pathMatcher.match(path, thisUri)) {
                    return true;
                }
            }
        }

        return false;
    }
}