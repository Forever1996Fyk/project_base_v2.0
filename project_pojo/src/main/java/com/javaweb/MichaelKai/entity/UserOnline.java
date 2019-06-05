package com.javaweb.MichaelKai.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: project_base
 * @description: 在线用户entity
 * @author: YuKai Fan
 * @create: 2019-06-05 16:55
 **/
@Data
public class UserOnline implements Serializable {

    //session会话id
    private String id;
    //用户id
    private String userId;
    //用户昵称
    private String nickName;
    //用户主机地址
    private String host;
    //用户登录时系统IP
    private String systemHost;
    //状态
    private Integer status;
    //session创建时间
    private String sessionStartTime;
    //session最后访问时间
    private String lastAccessTime;
    //超时时间
    private Long timeout;
}