package com.javaweb.MichaelKai.jwt.annotation;/**
 * Created by Administrator on 2019/6/24.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: project_base
 * @description: jwt权限注解(需要权限)
 * @author: YuKai Fan
 * @create: 2019-06-24 11:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JwtPermissions {
}
