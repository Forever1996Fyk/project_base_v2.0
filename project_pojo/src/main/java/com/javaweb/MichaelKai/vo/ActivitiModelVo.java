package com.javaweb.MichaelKai.vo;

import lombok.Data;

/**
 * @program: project_base
 * @description: activiti模型实体
 * @author: YuKai Fan
 * @create: 2019-08-05 14:31
 **/
@Data
public class ActivitiModelVo {

    //模型id
    private String id;
    //模型名称
    private String name;
    //模型描述
    private String description = "";
    //模型版本 默认为1
    private Integer revision = 1;
    //模型key
    private String key;
}