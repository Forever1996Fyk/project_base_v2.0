package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
public class UserRole implements Serializable{

	private Integer id;//
	private String user_id;//用户标识
	private String role_id;//角色标识

	
	public Integer getId() {		
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser_id() {		
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRole_id() {		
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}


	
}
