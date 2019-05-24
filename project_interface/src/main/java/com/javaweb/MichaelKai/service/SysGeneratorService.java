package com.javaweb.MichaelKai.service;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 代码生成service
 * @author: YuKai Fan
 * @create: 2019-05-24 08:54
 **/
public interface SysGeneratorService {

    /**
     * 生成所有表代码
     * @param pageNum
     * @param pageSize
     * @param map
     * @return
     */
    PageInfo<Map<String, Object>> pageList(int pageNum, int pageSize, Map<String, Object> map);

    /**
     * 根据表名查询表
     * @param tableName
     * @return
     */
    Map<String, String> queryTable(String tableName);

    /**
     * 根据表名查询列
     * @param tableName
     * @return
     */
    List<Map<String, String>> queryColumns(String tableName);

    /**
     * 生成代码
     * @param tableNames
     * @return
     */
    byte[] generatorCode(String[] tableNames);


}