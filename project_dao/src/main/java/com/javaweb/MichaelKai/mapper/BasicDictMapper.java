package com.javaweb.MichaelKai.mapper;

import com.javaweb.MichaelKai.pojo.Dict;
import com.javaweb.MichaelKai.pojo.DictItem;
import org.apache.ibatis.annotations.Param;

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
    void addDictItems(@Param(value = "list")List<DictItem> list);

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


    /**
     * 新增
     * @param dict 实体
     * @return
     */
    int addDict(Dict dict);


    /**
     * 根据id查询
     * @param id  主键
     * @return
     */
    Map<String, Object> getDictById(String id);

    /**
     * 根据id修改
     * @param dict 实体
     * @return
     */
    int editDictById(Dict dict);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int delDictById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delDictByIds(List<String> ids);

    /**
     * 真删除
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delDictRealById(String id);

    /**
     * 真批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delDictRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delDictReals();

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getDicts(Map<String, Object> map);
}