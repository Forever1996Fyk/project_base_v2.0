package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;
import com.javaweb.MichaelKai.pojo.DictItem;
import org.apache.ibatis.annotations.Param;


/**
 * 数据字典项
 * 
 * @author YuKai Fan
 * @date 2019-05-24 16:13:29
 */
public interface DictItemMapper {



    /**
     * 新增
     * @param dictItem 实体
     * @return
     */
    int addDictItem(DictItem dictItem);

    /**
     * 批量新增
     * @param list 实体集合
     */
    void addDictItems(@Param(value = "list") List<DictItem> list);

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
    int editDictItemById(DictItem dictItem);

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
    int delDictItemById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delDictItemByIds(List<String> ids);

    /**
     * 真删除
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delDictItemRealById(String id);

    /**
     * 真批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delDictItemRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delDictItemReals();

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getDictItems(Map<String, Object> map);

}
