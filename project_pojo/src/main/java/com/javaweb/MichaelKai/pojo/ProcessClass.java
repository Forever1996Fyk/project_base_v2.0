package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;

import lombok.Data;


/**
 * 流程分类表
 *
 * @author YuKai Fan
 * @create 2019-08-12 10:14:44
 */
@Data
public class ProcessClass implements Serializable {
    private static final long serialVersionUID = 1L;

    //分类标识
    private String id;
    //流程名称
    private String name;
    //流程类型
    private String type;
    //流程分类图标
    private String classIcon;
    //流程申请页面url
    private String applyUrl;
    //我的申请列表页面url
    private String applyListUrl;
    //我的待办列表页面url
    private String upcomingListUrl;
    //我的已办列表页面url
    private String alreadyListUrl;
    //状态
    private Integer status;


}
