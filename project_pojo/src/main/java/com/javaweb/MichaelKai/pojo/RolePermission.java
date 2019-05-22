package com.javaweb.MichaelKai.pojo;

import lombok.Data;

import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
@Data
public class RolePermission implements Serializable{

	private Integer id;//
	private String roleId;//角色标识
	private String permissionId;//权限标识



	
}
