package com.javaweb.MichaelKai.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @program: project_base
 * @description: 反射工具类
 * @author: YuKai Fan
 * @create: 2019-05-31 09:58
 **/
@Slf4j
public class ReflectUtil {

    /**
     * 利用反射获取指定对象的指定属性
     * @param obj 目标对象
     * @param fieldName 目标属性
     * @return 目标属性的值
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                log.error("获取目标属性值错误", e);
            }
        }
        return result;
    }

    /**
     * 利用反射获取指定对象的指定字段
     * @param obj 目标对象
     * @param fieldName 目标属性
     * @return 目标字段
     */
    private static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                // 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
            }
        }
        return field;
    }

    /**
     * 利用反射设置指定对象的指定属性为指定值
     * @param object
     * @param fieldName
     * @param fieldValue
     */
    public static void setFieldValue(Object object, String fieldName, String fieldValue) {
        Field field = ReflectUtil.getField(object, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(object, fieldValue);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                log.error("设置目标属性值错误", e);
            }
        }
    }

}