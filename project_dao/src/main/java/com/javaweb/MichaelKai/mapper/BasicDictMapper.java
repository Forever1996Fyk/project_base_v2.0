package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-05-24 16:26
 **/
public interface BasicDictMapper {
    /**
     * 根据参数获取数据字典项
     * @param map
     * @return
     */
    List<Map<String, Object>> getDictItemsByParam(Map<String, Object> map);

}