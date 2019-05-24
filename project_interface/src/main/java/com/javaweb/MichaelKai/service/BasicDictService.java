package com.javaweb.MichaelKai.service;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-05-24 16:21
 **/
public interface BasicDictService {

    /**
     * 获取数据字典项
     * @param map
     * @return
     */
    List<Map<String, Object>> getDictItem(Map<String, Object> map);

    /**
     * 添加数据字典
     * @param map
     */
    void addDict(Map<String, Object> map);

}