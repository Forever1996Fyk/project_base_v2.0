package com.javaweb.MichaelKai.common.enums;

import lombok.Getter;

/**
 * @program: project_base
 * @description: 菜单类型枚举
 * @author: YuKai Fan
 * @create: 2019-05-21 13:39
 **/
@Getter
public enum FileTypeEnum {
    PIC(0, "图片"),
    FILE(1, "文件");

    private Integer attachType;

    private String message;

    FileTypeEnum(Integer attachType, String message) {
        this.attachType = attachType;
        this.message = message;
    }

}