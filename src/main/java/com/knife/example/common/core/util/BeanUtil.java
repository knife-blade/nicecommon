package com.knife.example.common.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BeanUtil {
    /**
     * 浅拷贝
     */
    public static <T> T copy(Object source, Class<T> target) {
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
     * 浅拷贝
     */
    public static <T> List<T> copy(List<?> sources, Class<T> target) {
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

    /**
     * 深拷贝
     */
    public static <T> T deepCopy(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        String json = JsonUtil.toJson(source);
        return JsonUtil.toObject(json, target);
    }

    /**
     * 深拷贝
     * 此处第二个参数必须用TypeReference，如果用Class<T> 会导致泛型擦除，最后返回的是个List<LinedHashMap>
     */
    public static <T> List<T> deepCopy(List<?> sources, TypeReference<List<T>> typeReference) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }
        String json = JsonUtil.toJson(sources);
        return JsonUtil.toObjectList(json, typeReference);
    }

    /**
     * 把对象中的 String 类型的null字段，转换为指定字符串，比如：空字符串
     *
     * @param <T> 待转化对象类型
     * @param cls 待转化对象
     * @param str 目标字符串
     */
    public static <T> void replaceNullString(T cls, String str) {
        Field[] fields = cls.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return;
        }
        for (Field field : fields) {
            if ("String".equals(field.getType().getSimpleName())) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(cls);
                    if (!StringUtils.hasText(value)) {
                        field.set(cls, str);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断是否所有字段都是null
     */
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