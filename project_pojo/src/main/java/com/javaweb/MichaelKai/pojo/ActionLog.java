package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;
import lombok.Data;


/**
 * 用户日志
 * 
 * @author YuKai Fan
 * @create 2019-05-31 15:39:53
 */
@Data
public class ActionLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键ID
		private String id;
		    //日志名称
		private String name;
		    //日志类型
		private String type;
		    //操作IP地址
		private String ipaddr;
		    //产生日志的类
		private String clazz;
		    //产生日志的方法
		private String method;
		    //产生日志的表
		private String model;
		    //产生日志的数据id
		private Long recordId;
		    //日志消息
		private String message;

		private String createUserId;
		private String createTime;
		private String updateUserId;
		private String updateTime;
	

}
