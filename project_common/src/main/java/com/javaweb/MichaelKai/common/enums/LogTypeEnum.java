package com.javaweb.MichaelKai.common.enums;

import lombok.Getter;

/**
 * @program: project_base
 * @description: 日志类型枚举
 * @author: YuKai Fan
 * @create: 2019-05-31 16:49
 **/
@Getter
public enum  LogTypeEnum {

    /**
     * 用户有关日志：0开头
     */
    USER_LOGIN("00", "用户登录"),
    USER_MANAGER("01", "用户管理"),
    USER_ADD("02", "用户新增"),
    USER_EDIT("03", "用户修改"),
    USER_DEL("04", "用户删除");

    private String type;

    private String message;

    LogTypeEnum(String type, String message) {
        this.type = type;
        this.message = message;
    }
}