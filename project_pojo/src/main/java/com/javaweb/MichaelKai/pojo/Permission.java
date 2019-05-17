package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
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

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {		
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {		
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getSort() {		
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getLevel() {		
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getRemark() {		
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {		
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getPerm() {		
		return perm;
	}
	public void setPerm(String perm) {
		this.perm = perm;
	}


	
}
