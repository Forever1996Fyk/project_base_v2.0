package com.javaweb.MichaelKai.common.enums;

import lombok.Getter;

/**
 * @program: project_base
 * @description: jwt验证结果枚举
 * @author: YuKai Fan
 * @create: 2019-06-24 11:42
 **/
@Getter
public enum JwtResultEnums {
    /**
     * token问题
     */
    TOKEN_ERROR(301, "token无效"),
    TOKEN_EXPIRED(302, "token已过期"),
    TOKEN_REFRESH(200, "token刷新"),

    /**
     * 用户注销
     */
    TOKEN_LOGOUT(401, "token已注销"),

    /**
     * 账号问题
     */
    AUTH_REQUEST_ERROR(401, "用户名或密码错误"),
    AUTH_REQUEST_LOCKED(402, "该账号已被冻结"),
    ;

    private Integer code;

    private String message;

    JwtResultEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}