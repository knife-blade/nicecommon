package com.knife.common.advice;

import com.knife.common.entity.Result;
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
    public Result<Object> handleException(Exception e) throws Exception {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        // 实际项目中应该这样写，防止用户看到详细的异常信息
        // return new Result().failure().message.message("操作失败");
        return new Result<>().failure().message(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<Object> handleBusinessException(BusinessException e) throws Exception {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        // 实际项目中应该这样写，防止用户看到详细的异常信息
        // return new Result<>().failure().message("操作失败");
        return new Result<>().failure().message(e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public Result<Object> handleSystemException(SystemException e) throws Exception {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        // 实际项目中应该这样写，防止用户看到详细的异常信息
        // return new Result<>().failure().message("操作失败");
        return new Result<>().failure().message(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public Result<Object> handleNullPointerException(NullPointerException e) throws Exception {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        // 实际项目中应该这样写，防止用户看到详细的异常信息
        // return new Result<>().failure().message("操作失败");
        String message = ThrowableUtil.getStackTraceByPackage(e, "com.knife");
        return new Result<>().failure().message(message);
    }
}