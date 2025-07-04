package com.suchtool.nicecommon.core.annotation;
 
import java.lang.annotation.*;

/**
 * 不自动包装返回值
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseWrapperIgnore {

}