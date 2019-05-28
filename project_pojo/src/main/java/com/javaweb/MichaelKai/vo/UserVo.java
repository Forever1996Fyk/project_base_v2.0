package com.javaweb.MichaelKai.vo;

import lombok.Data;

/**
 * @program: project_base
 * @description: 用户登录实体类
 * @author: YuKai Fan
 * @create: 2019-05-27 11:38
 **/
@Data
public class UserVo {
    private String account;
    private String password;
    private String captcha;//验证码
}