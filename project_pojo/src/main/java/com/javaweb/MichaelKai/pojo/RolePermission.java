package com.javaweb.MichaelKai.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 实体类
 * @author Administrator
 *
 */
@Data
public class RolePermission implements Serializable{

	private Integer id;//
	private String roleId;//角色标识
	private Set<String> permissionIds;//权限标识



	
}
