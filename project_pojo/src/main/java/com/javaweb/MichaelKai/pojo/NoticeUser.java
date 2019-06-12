package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;
import lombok.Data;


/**
 * 通知通告用户表
 * 
 * @author YuKai Fan
 * @create 2019-06-11 15:54:42
 */
@Data
public class NoticeUser implements Serializable {
	private static final long serialVersionUID = 1L;
	
		private Integer id;
		    //通告标识
		private String noticeId;
		    //用户标识
		private String userId;
		    //状态:0  未读 1 已读
		private Integer readed;
		    //阅读时间
		private String readedTime;
		    //备注
		private String remark;
	

}
