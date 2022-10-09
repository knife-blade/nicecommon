package com.knife.common.advice;

import com.knife.common.entity.ResultWrapper;
import com.knife.common.exception.BusinessException;
import com.knife.common.exception.SystemException;
import com.knife.common.util.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResultWrapper<Object> handleException(Exception e) throws Exception {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.failure(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResultWrapper<Object> handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.failure(e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public ResultWrapper<Object> handleSystemException(SystemException e) {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.failure(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResultWrapper<Object> handleNullPointerException(NullPointerException e) {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        String message = ThrowableUtil.getLastStackTrace(e, null);
        return ResultWrapper.failure(message);
    }
}