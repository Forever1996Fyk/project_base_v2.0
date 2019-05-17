package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
public class Attachment implements Serializable{

	private String id;//附件标识
	private String attach_md5;//文件md5
	private String attach_sha1;//文件SHA1值
	private String attach_origin_title;//文件原名
	private String attach_utily;//附件属性，例身份证证明
	private Integer attach_type;//附件类型0图片1附件
	private String attach_name;//文件名
	private Long attach_size;//文件大小
	private String attach_postfix;//文件后缀
	private String attachment;//附件
	private String remark;//备注
	private Integer status;//状态:0  已禁用 1 正在使用
	private String attach_path;//文件路径

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getAttach_md5() {		
		return attach_md5;
	}
	public void setAttach_md5(String attach_md5) {
		this.attach_md5 = attach_md5;
	}

	public String getAttach_sha1() {		
		return attach_sha1;
	}
	public void setAttach_sha1(String attach_sha1) {
		this.attach_sha1 = attach_sha1;
	}

	public String getAttach_origin_title() {		
		return attach_origin_title;
	}
	public void setAttach_origin_title(String attach_origin_title) {
		this.attach_origin_title = attach_origin_title;
	}

	public String getAttach_utily() {		
		return attach_utily;
	}
	public void setAttach_utily(String attach_utily) {
		this.attach_utily = attach_utily;
	}

	public Integer getAttach_type() {		
		return attach_type;
	}
	public void setAttach_type(Integer attach_type) {
		this.attach_type = attach_type;
	}

	public String getAttach_name() {		
		return attach_name;
	}
	public void setAttach_name(String attach_name) {
		this.attach_name = attach_name;
	}

	public Long getAttach_size() {		
		return attach_size;
	}
	public void setAttach_size(Long attach_size) {
		this.attach_size = attach_size;
	}

	public String getAttach_postfix() {		
		return attach_postfix;
	}
	public void setAttach_postfix(String attach_postfix) {
		this.attach_postfix = attach_postfix;
	}

	public String getAttachment() {		
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
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

	public String getAttach_path() {
		return attach_path;
	}
	public void setAttach_path(String attach_path) {
		this.attach_path = attach_path;
	}


	
}
