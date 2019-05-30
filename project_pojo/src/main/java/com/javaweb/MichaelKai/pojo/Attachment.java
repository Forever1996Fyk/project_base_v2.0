package com.javaweb.MichaelKai.pojo;

import java.io.Serializable;


/**
 * 
 * 
 * @author YuKai Fan
 * @create 2019-05-20 16:53:28
 */
public class Attachment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//附件标识
	private String id;
	//文件md5
	private String attachMd5;
	//文件SHA1值
	private String attachSha1;
	//文件原名
	private String attachOriginTitle;
	//附件属性，例身份证证明
	private String attachUtily;
	//附件类型0图片1附件
	private Integer attachType;
	//文件名
	private String attachName;
	//文件大小
	private Long attachSize;
	//文件后缀
	private String attachPostfix;
	//附件
	private byte[] attachment;
	//备注
	private String remark;
	//状态:0  已禁用 1 正在使用
	private Integer status;
	//文件路径
	private String attachPath;
	//创建人
	private String createUserId;
	//更新人
	private String updateUserId;
	private String createTime;
	private String updateTime;
	
	/**
	 * 设置：附件标识
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：附件标识
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：文件md5
	 */
	public void setAttachMd5(String attachMd5) {
		this.attachMd5 = attachMd5;
	}
	/**
	 * 获取：文件md5
	 */
	public String getAttachMd5() {
		return attachMd5;
	}
	/**
	 * 设置：文件SHA1值
	 */
	public void setAttachSha1(String attachSha1) {
		this.attachSha1 = attachSha1;
	}
	/**
	 * 获取：文件SHA1值
	 */
	public String getAttachSha1() {
		return attachSha1;
	}
	/**
	 * 设置：文件原名
	 */
	public void setAttachOriginTitle(String attachOriginTitle) {
		this.attachOriginTitle = attachOriginTitle;
	}
	/**
	 * 获取：文件原名
	 */
	public String getAttachOriginTitle() {
		return attachOriginTitle;
	}
	/**
	 * 设置：附件属性，例身份证证明
	 */
	public void setAttachUtily(String attachUtily) {
		this.attachUtily = attachUtily;
	}
	/**
	 * 获取：附件属性，例身份证证明
	 */
	public String getAttachUtily() {
		return attachUtily;
	}
	/**
	 * 设置：附件类型0图片1附件
	 */
	public void setAttachType(Integer attachType) {
		this.attachType = attachType;
	}
	/**
	 * 获取：附件类型0图片1附件
	 */
	public Integer getAttachType() {
		return attachType;
	}
	/**
	 * 设置：文件名
	 */
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	/**
	 * 获取：文件名
	 */
	public String getAttachName() {
		return attachName;
	}
	/**
	 * 设置：文件大小
	 */
	public void setAttachSize(Long attachSize) {
		this.attachSize = attachSize;
	}
	/**
	 * 获取：文件大小
	 */
	public Long getAttachSize() {
		return attachSize;
	}
	/**
	 * 设置：文件后缀
	 */
	public void setAttachPostfix(String attachPostfix) {
		this.attachPostfix = attachPostfix;
	}
	/**
	 * 获取：文件后缀
	 */
	public String getAttachPostfix() {
		return attachPostfix;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
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
	/**
	 * 设置：文件路径
	 */
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
	/**
	 * 获取：文件路径
	 */
	public String getAttachPath() {
		return attachPath;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
