package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
public class RolePermission implements Serializable{

	private Integer id;//
	private String role_id;//角色标识
	private String permission_id;//权限标识

	
	public String getRole_id() {		
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getPermission_id() {		
		return permission_id;
	}
	public void setPermission_id(String permission_id) {
		this.permission_id = permission_id;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}


	
}
