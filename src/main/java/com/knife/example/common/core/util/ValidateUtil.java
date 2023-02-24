package com.knife.example.common.core.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * hibernate validator的校验工具
 */
public class ValidateUtil {
    private static final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 校验实体类
     */
    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (constraintViolations.size() > 0) {
            StringBuilder validateError = new StringBuilder();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                validateError.append(constraintViolation.getMessage()).append(";");
            }

            throw new ValidationException(validateError.toString());
        }
    }

    /**
     * 通过组来校验实体类
     */
    public static <T> void validate(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t, groups);
        if (constraintViolations.size() > 0) {
            StringBuilder validateError = new StringBuilder();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                validateError.append(constraintViolation.getMessage()).append(";");
            }

            throw new ValidationException(validateError.toString());
        }
    }
}
