package com.javaweb.MichaelKai.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @program: project_base
 * @description: 通知通告用户实体
 * @author: YuKai Fan
 * @create: 2019-06-11 09:38
 **/
@Data
public class NoticeVo implements Serializable {
    private Integer id;//
    private String noticeId;//通知通告标识
    private Set<String> userIds;//用户标识
}