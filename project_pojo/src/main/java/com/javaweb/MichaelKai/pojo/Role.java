package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
public class Role implements Serializable{
	private String id;//角色标识
	private String role_name;//角色名称
	private String remark;//备注
	private Integer status;//状态:0  已禁用 1 正在使用

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getRole_name() {		
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
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


	
}
