package com.javaweb.MichaelKai.service;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.pojo.DictItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 数据字典项
 * @author: YuKai Fan
 * @create: 2019-05-24 16:13:29
 **/
public interface DictItemService {


  /**
    * 新增
    * @param dictItem 实体
    * @return
    */
   DictItem addDictItem(DictItem dictItem);

   /**
    * 根据id查询
    * @param id  主键
    * @return
    */
   Map<String, Object> getDictItemById(String id);

   /**
    * 根据id修改
    * @param dictItem 实体
    * @return
    */
   void editDictItemById(DictItem dictItem);

   /**
    * 批量修改
    *
    * @param dictItem 实体
    * @param ids 主键集合
    */
   void editDictItemByIds(@Param("map") DictItem dictItem, @Param("list") List<String> ids);

   /**
    * 根据id删除
    * @param id
    * @return
    */
   void delDictItemById(String id);

   /**
    * 批量删除
    *
    * @param ids 主键集合
    * @return dao成功失败标志
    */
   void delDictItemByIds(List<String> ids);

   /**
    * 获取所有(分页)
    * @param start 开始记录
    * @param pageSize 分页大小
    * @param map 参数
    * @return
    */
   PageInfo<Map<String, Object>> getDictItems(int start, int pageSize, Map<String, Object> map);

   /**
    * 获取所有数据.
    * @param map 页面表单
    * @return 结果集合
    */
   List<Map<String, Object>> getDictItems(Map<String, Object> map);
}
