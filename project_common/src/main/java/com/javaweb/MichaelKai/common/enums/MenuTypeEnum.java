package com.javaweb.MichaelKai.common.enums;

import lombok.Getter;

/**
 * @program: project_base
 * @description: 菜单类型枚举
 * @author: YuKai Fan
 * @create: 2019-05-21 13:39
 **/
@Getter
public enum MenuTypeEnum {
    TOP_LEVEL(1, "一级菜单"),
    SUB_LEVEL(2, "二级菜单"),
    NOT_MENU(3, "不是菜单");

    private Integer level;

    private String message;

    MenuTypeEnum(Integer level, String message) {
        this.level = level;
        this.message = message;
    }

}