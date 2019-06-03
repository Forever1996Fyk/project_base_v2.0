package com.javaweb.MichaelKai.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体类
 * @author Administrator
 *
 */
@Data
public class Permission implements Serializable{

	private String id;//权限标识
	private String name;//权限名称
	private String pid;//上级权限id
	private Integer sort;//排名
	private Integer level;//等级
	private String remark;//备注
	private Integer status;//状态:0  已禁用 1 正在使用
	private String url;//链接
	private String perm;//权限标识
	private String icon;//图标
	private Map<String, Object> children = new HashMap<>();//下级权限实体
}
