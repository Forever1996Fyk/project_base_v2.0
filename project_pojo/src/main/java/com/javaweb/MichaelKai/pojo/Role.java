package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;


/**
 * 角色表
 * 
 * @author YuKai Fan
 * @create 2019-05-20 11:51:00
 */
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//角色标识
	private String id;
	//角色名称
	private String roleName;
	//备注
	private String remark;
	//状态:0  已禁用 1 正在使用
	private Integer status;

	/**
	 * 设置：角色标识
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：角色标识
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：角色名称
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * 获取：角色名称
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：状态:0  已禁用 1 正在使用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态:0  已禁用 1 正在使用
	 */
	public Integer getStatus() {
		return status;
	}
}
