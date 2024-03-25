package com.suchtool.nicecommon.core.advice;

import com.suchtool.nicecommon.core.entity.ResultWrapper;
import com.suchtool.nicecommon.core.exception.AuthenticationException;
import com.suchtool.nicecommon.core.exception.BusinessException;
import com.suchtool.nicecommon.core.exception.SystemException;
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

        // 若某个自定义异常有@ResponseStatus注解，就继续抛出，这样状态码就能正常响应
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.error().message(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResultWrapper<?> handleNullPointerException(NullPointerException e) {
        return ResultWrapper.error().message("空指针异常，请联系客服");
    }

    /**
     * 数据校验异常
     */
    @ExceptionHandler(BindException.class)
    public ResultWrapper<?> handleBindException(BindException e) {
        String message = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResultWrapper.error().message(message);
    }

    @ExceptionHandler(BusinessException.class)
    public ResultWrapper<?> handleBusinessException(BusinessException e) {
        return ResultWrapper.error().message(e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public ResultWrapper<?> handleSystemException(SystemException e) {
        return ResultWrapper.error().message("系统异常，请联系客服");
    }
}