package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;
import lombok.Data;


/**
 * 通知通告表
 * 
 * @author YuKai Fan
 * @create 2019-06-10 09:56:24
 */
@Data
public class Notice implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //通告标识
		private String id;
		    //通告标题
		private String title;
		    //通告内容
		private String content;
		    //发布时间
		private String publishTime;
		    //是否撤销:0  已撤销 1 正在使用
		private Integer cancel;
		    //撤销时间
		private String cancelTime;
		    //优先级
		private Integer priority;
		    //备注
		private String remark;
		    //状态:0  已禁用 1 正在使用 2 已删除
		private Integer status;
	

}
