package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;

import lombok.Data;


/**
 * 请假流程表
 *
 * @author YuKai Fan
 * @create 2019-08-08 10:30:23
 */
@Data
public class UserLeave extends BaseTask implements Serializable {
    private static final long serialVersionUID = 1L;

    //请假标识
    private String id;
    //用户标识
    private String userId;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //请假原因
    private String reason;
    //请假天数
    private Integer leaveDays;
    //流程实例id
    private String processInstanceId;
    //用户请假路径
    private String urlPath;
    //状态:0  已禁用 1 正在使用
    private Integer status;
    //流程定义key
    private String processDefinitionKey;


}
