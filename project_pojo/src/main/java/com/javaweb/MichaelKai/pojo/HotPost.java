package com.javaweb.MichaelKai.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;


/**
 * 热帖表
 * 
 * @author YuKai Fan
 * @create 2019-07-01 17:05:29
 */
@Data
@Document(indexName = HotPost.INDEX, type = HotPost.ORDER_TYPE, shards = 6, replicas = 2, refreshInterval = "-1")
@Mapping(mappingPath = "hotPostSearch_mapping.json")
public class HotPost implements Serializable {
	private static final long serialVersionUID = 1L;

	//建立索引
	public static final String INDEX = "hot_post_index";
	//类型
	public static final String ORDER_TYPE = "hot_post";
	
	//热帖标识
	@Id
	private String id;
	//热帖标题
	@Field(type = FieldType.text, searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
	private String title;
	//热帖内容
	@Field(type = FieldType.text, searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
	private String content;
	//热帖类型
	private Integer type;
	//排名
	private Integer sort;
	//备注
	private String remark;
	//状态:0  已禁用 1 正在使用
	private Integer status;
	

}
