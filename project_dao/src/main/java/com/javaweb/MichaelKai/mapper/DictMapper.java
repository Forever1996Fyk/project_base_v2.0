package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;
import com.javaweb.MichaelKai.pojo.Dict;
import org.apache.ibatis.annotations.Param;


/**
 * 数据字典
 * 
 * @author YuKai Fan
 * @date 2019-05-24 16:13:29
 */
public interface DictMapper {



    /**
     * 新增
     * @param dict 实体
     * @return
     */
    int addDict(Dict dict);

    /**
     * 批量新增
     * @param list 实体集合
     */
    void addDicts(@Param(value = "list") List<Dict> list);

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
     * 批量修改
     *
     * @param dict 实体
     * @param ids 主键集合
     */
    void editDictByIds(@Param("map") Dict dict, @Param("list") List<String> ids);

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
