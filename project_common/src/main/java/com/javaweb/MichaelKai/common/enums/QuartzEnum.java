package com.javaweb.MichaelKai.common.enums;

import lombok.Getter;

/**
 * @program: project_base
 * @description: quartz任务枚举类
 * @author: YuKai Fan
 * @create: 2019-07-01 11:41
 **/
@Getter
public enum QuartzEnum {
    /**
     * cron问题
     */
    CRON_NOT_NULL(301, "cron不能为空"),
    CRON_VALID_ERROR(302, "cron表达式错误"),
    ;

    private Integer code;

    private String message;

    QuartzEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}