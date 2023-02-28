package com.knife.example.common.core.util;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BeanHelper {
    public static <T> List<T> convert(List<?> sources, Class<T> target) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }

        List<T> targets = new LinkedList<>();
        for (Object source : sources) {
            T t = null;
            try {
                t = target.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            BeanUtils.copyProperties(source, t);
            targets.add(t);
        }

        return targets;
    }

    public static <T> T convert(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }

        T t;
        try {
            t = target.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        BeanUtils.copyProperties(source, t);
        return t;
    }

    /**
     * 把对象中的 String 类型的null字段，转换为空字符串
     */
    public static void nullStringToBlank(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();

        for (Field field : fields) {
            if ("String".equals(field.getType().getSimpleName())) {
                field.setAccessible(true);
                try {
                    Object value = field.get(o);
                    if (value == null) {
                        field.set(o, "");
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean allFieldAreNull(Object o) {
        Class<?> aClass = o.getClass();

        PropertyDescriptor[] beanProperties = ReflectUtils.getBeanProperties(aClass);
        for (PropertyDescriptor beanProperty : beanProperties) {
            Method readMethod = beanProperty.getReadMethod();
            try {
                Object value = readMethod.invoke(o);
                if (value != null) {
                    return false;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}
