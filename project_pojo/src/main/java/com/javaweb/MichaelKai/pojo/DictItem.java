package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;
import lombok.Data;


/**
 * 数据字典项
 * 
 * @author YuKai Fan
 * @create 2019-05-24 16:13:29
 */
@Data
public class DictItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //字典项标识
		private String id;
		    //字典标识
		private String dicId;
		    //字典项编码
		private String itemCode;
		    //字典项名称
		private String itemName;
		    //排序
		private Integer itemSort;
		    //状态:0  已禁用 1 正在使用
		private Integer status;

		private String remark;
	

}
