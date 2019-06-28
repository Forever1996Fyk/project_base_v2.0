package com.javaweb.MichaelKai.pojo;

import lombok.Data;

import java.io.Serializable;


/**
 * 
 * 定时任务
 * @author YuKai Fan
 * @create 2019-06-27 11:00:32
 */
@Data
public class ScheduleJob implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	//任务id
	private String jobId;
	//任务name
	private String jobName;
	//任务状态
	private String jobStatus;
	//任务组name
	private String jobGroup;
	//cron表达式
	private String cronExpression;
	//藐视
	private String description;
	//bean名称
	private String beanName;
	//方法名称
	private String methodName;
	//上次执行时间
	private String lastTriggerTime;
	//下次执行时间
	private String nextTriggerTime;

	private String createUserId;

	private String createTime;

	private String updateUserId;

	private String updateTime;


}
