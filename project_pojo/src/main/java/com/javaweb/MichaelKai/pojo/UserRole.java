package com.javaweb.MichaelKai.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 * @author Administrator
 *
 */
@Data
public class UserRole implements Serializable{

	private Integer id;//
	private String userId;//用户标识
	private String roleId;//角色标识

}
