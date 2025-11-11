package com.suchtool.nicecommon.core.advice;

import com.suchtool.nicecommon.core.exception.BusinessException;
import com.suchtool.nicecommon.core.exception.CatchException;
import com.suchtool.nicecommon.core.exception.CustomResultException;
import com.suchtool.nicecommon.core.exception.SystemException;
import com.suchtool.nicecommon.core.model.ResultWrapper;
import com.suchtool.nicecommon.core.property.NiceCommonGlobalExceptionProperty;
import com.suchtool.nicelog.util.log.NiceLogUtil;
import com.suchtool.nicetool.util.base.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class NiceCommonGlobalExceptionAdvice implements Ordered {
    private final int order;

    private final Boolean enableGlobalExceptionAdviceLog;

    public NiceCommonGlobalExceptionAdvice(NiceCommonGlobalExceptionProperty property) {
        this.order = property.getAdviceOrder();
        this.enableGlobalExceptionAdviceLog = property.getEnableLog();
    }

    @Override
    public int getOrder() {
        return order;
    }

    @ExceptionHandler(Exception.class)
    public ResultWrapper<?> handleException(Exception e) throws Exception {
        String errorMessage = process(e, "异常");

        // 若某个自定义异常有@ResponseStatus注解，就继续抛出，这样状态码就能正常响应
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.error().message(errorMessage);
    }

    @ExceptionHandler(Error.class)
    public ResultWrapper<?> handleError(Error e) {
        String errorMessage = process(e, "错误");

        // 若某个自定义异常有@ResponseStatus注解，就继续抛出，这样状态码就能正常响应
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.error().message(errorMessage);
    }


    @ExceptionHandler(BusinessException.class)
    public ResultWrapper<?> handleBusinessException(BusinessException e) {
        String errorMessage = process(e, "业务异常");

        return ResultWrapper.error().message(errorMessage);
    }

    @ExceptionHandler(CatchException.class)
    public ResultWrapper<?> handleCatchException(CatchException e) {
        String errorMessage = process(e, "捕获异常");

        return ResultWrapper.error().message(errorMessage);
    }

    @ExceptionHandler(SystemException.class)
    public ResultWrapper<?> handleSystemException(SystemException e) {
        String errorMessage = process(e, "系统异常");

        return ResultWrapper.error().message(errorMessage);
    }

    @ExceptionHandler(CustomResultException.class)
    public ResultWrapper<?> handleCustomResultException(CustomResultException e) {
        process(e, "自定义返回值异常", false);

        return e.getResult();
    }

    @ExceptionHandler(NullPointerException.class)
    public ResultWrapper<?> handleNullPointerException(NullPointerException e) {
        process(e, "空指针异常");

        return ResultWrapper.error().message("空指针异常");
    }

    /**
     * 数据校验异常
     */
    @ExceptionHandler(BindException.class)
    public ResultWrapper<?> handleBindException(BindException e) {
        String errorInfo = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        if (enableGlobalExceptionAdviceLog) {
            NiceLogUtil.createBuilder()
                    .mark("数据校验异常")
                    .errorInfo(errorInfo)
                    .throwable(e)
                    .error();
        }

        return ResultWrapper.error().message(errorInfo);
    }

    private String process(Throwable throwable, String mark) {
        return process(throwable, mark, true);
    }

    private String process(Throwable throwable, String mark, Boolean isError) {
        Throwable causeThrowable = null;
        String lastStackTraceString = null;
        String errorMessage = null;

        if (throwable.getCause() == null) {
            causeThrowable = throwable;
            errorMessage = throwable.getMessage();
        } else {
            causeThrowable = throwable.getCause();
            lastStackTraceString = ThrowableUtil.stackTraceToString(causeThrowable);

            errorMessage = throwable.getMessage();
            if (!StringUtils.hasText(errorMessage)) {
                errorMessage = causeThrowable.getMessage();
            }
        }

        if (enableGlobalExceptionAdviceLog) {
            if (Boolean.TRUE.equals(isError)) {
                NiceLogUtil.createBuilder()
                        .mark(mark)
                        .message(errorMessage)
                        .throwable(causeThrowable)
                        .errorInfo(lastStackTraceString)
                        .error();
            } else {
                NiceLogUtil.createBuilder()
                        .mark(mark)
                        .throwable(causeThrowable)
                        .info();
            }
        }

        return errorMessage;
    }
}