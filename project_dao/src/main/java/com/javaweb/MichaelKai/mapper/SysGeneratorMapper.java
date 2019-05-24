package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * @author earl
 * @date 2017-05-18 11:15
 */
public interface SysGeneratorMapper {

	List<Map<String, Object>> pageList(Map<String, Object> map);

	Map<String, String> queryTable(String tableName);
	
	List<Map<String, String>> queryColumns(String tableName);
}
