package com.knife.example.common.core.advice;

import com.knife.example.common.core.entity.ResultWrapper;
import com.knife.example.common.core.exception.BusinessException;
import com.knife.example.common.core.exception.ForbiddenException;
import com.knife.example.common.core.exception.SystemException;
import com.knife.example.common.core.util.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResultWrapper<?> handleException(Exception e) throws Exception {
        log.error(e.getMessage(), e);

        // 若某个自定义异常有@ResponseStatus注解，就继续抛出，这样状态码就能正常响应
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.error().message(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResultWrapper<?> handleNullPointerException(NullPointerException e) {
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

        return ResultWrapper.error().message(e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public ResultWrapper<?> handleSystemException(SystemException e) {
        log.error(e.getMessage(), e);

        return ResultWrapper.error().message("系统异常，请联系客服");
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResultWrapper<?> handleForbiddenException(ForbiddenException e) {
        log.error(e.getMessage(), e);

        // 若某个自定义异常有@ResponseStatus注解，就继续抛出，这样状态码就能正常响应
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.error().message("无权访问，请联系客服");
    }
}