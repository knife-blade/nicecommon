package com.knife.example.common.core.advice;

import com.knife.example.common.core.entity.ResultWrapper;
import com.knife.example.common.core.exception.BusinessException;
import com.knife.example.common.core.exception.SystemException;
import com.knife.example.common.core.util.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResultWrapper<?> handleException(Exception e) throws Exception {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.error().message(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResultWrapper<?> handleNullPointerException(NullPointerException e) {
        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        String message = ThrowableUtil.getLastStackTrace(e, null);
        log.error(message);

        return ResultWrapper.error().message("空指针异常，请联系客服");
    }

    /**
     * 数据校验异常
     */
    @ExceptionHandler(BindException.class)
    public ResultWrapper<?> handleBindException(BindException e) {
        log.error(e.getMessage(), e);

        String message = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResultWrapper.error().message(message);
    }

    @ExceptionHandler(BusinessException.class)
    public ResultWrapper<?> handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.error().message(e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public ResultWrapper<?> handleSystemException(SystemException e) {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.error().message("系统异常，请联系客服");
    }

}