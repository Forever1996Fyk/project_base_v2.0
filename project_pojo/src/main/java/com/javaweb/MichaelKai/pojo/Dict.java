package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;
import lombok.Data;


/**
 * 数据字典
 * 
 * @author YuKai Fan
 * @create 2019-05-24 16:13:29
 */
@Data
public class Dict implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //字典标识
		private String id;
		    //字典编码
		private String dicCode;
		    //字典名称
		private String dicName;
		//状态:0  已禁用 1 正在使用
		private Integer status;
		private String remark;
	

}
