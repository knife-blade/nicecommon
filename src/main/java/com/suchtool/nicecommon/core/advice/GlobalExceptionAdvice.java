package com.suchtool.nicecommon.core.advice;

import com.suchtool.nicecommon.core.entity.ResultWrapper;
import com.suchtool.nicecommon.core.exception.BusinessException;
import com.suchtool.nicecommon.core.exception.CustomCodeException;
import com.suchtool.nicecommon.core.exception.SystemException;
import com.suchtool.nicelog.util.log.NiceLogUtil;
import com.suchtool.nicetool.util.base.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice implements Ordered {
    private final int order;

    public GlobalExceptionAdvice(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @ExceptionHandler(Exception.class)
    public ResultWrapper<?> handleException(Exception e) throws Exception {
        NiceLogUtil.createBuilder()
                .mark("异常")
                .errorInfo(e.getMessage())
                .throwable(e)
                .error();

        // 若某个自定义异常有@ResponseStatus注解，就继续抛出，这样状态码就能正常响应
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.error().message(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResultWrapper<?> handleNullPointerException(NullPointerException e) {
        NiceLogUtil.createBuilder()
                .mark("空指针异常")
                .errorInfo(e.getMessage())
                .throwable(e)
                .error();

        return ResultWrapper.error().message("空指针异常，请联系客服");
    }

    /**
     * 数据校验异常
     */
    @ExceptionHandler(BindException.class)
    public ResultWrapper<?> handleBindException(BindException e) {
        String errorInfo = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        NiceLogUtil.createBuilder()
                .mark("数据校验异常")
                .errorInfo(errorInfo)
                .throwable(e)
                .error();

        return ResultWrapper.error().message(errorInfo);
    }

    @ExceptionHandler(BusinessException.class)
    public ResultWrapper<?> handleBusinessException(BusinessException e) {
        NiceLogUtil.createBuilder()
                .mark("业务异常")
                .errorInfo(e.getMessage())
                .throwable(e)
                .message(e.getCause() != null
                        ? ThrowableUtil.stackTraceToString(e.getCause())
                        : null)
                .error();

        return ResultWrapper.error().message(e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public ResultWrapper<?> handleSystemException(SystemException e) {
        NiceLogUtil.createBuilder()
                .mark("系统异常")
                .errorInfo(e.getMessage())
                .throwable(e)
                .error();

        return ResultWrapper.error().message("系统异常，请联系客服");
    }

    @ExceptionHandler(CustomCodeException.class)
    public ResultWrapper<?> handleCustomCodeException(CustomCodeException e) {
        NiceLogUtil.createBuilder()
                .mark("自定义编码异常")
                .errorInfo(e.getMessage())
                .throwable(e)
                .error();

        return ResultWrapper.success().code(e.getCode()).message(e.getMessage());
    }
}