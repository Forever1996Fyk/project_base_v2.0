package com.javaweb.MichaelKai.annotation;

import com.javaweb.MichaelKai.common.enums.LogTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志
 * Created by Administrator on 2019/5/31.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionLog {
    /**
     * 日志名称
     */
    String name() default "";

    /**
     * 日志类型
     */
    LogTypeEnum LOG_TYPE_ENUM();
}
