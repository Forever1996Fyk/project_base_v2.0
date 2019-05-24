package com.javaweb.MichaelKai.service;

import com.javaweb.MichaelKai.pojo.Dict;
import org.apache.ibatis.annotations.Param;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

 /**
  * @program: project_base
  * @description: 数据字典
  * @author: YuKai Fan
  * @create: 2019-05-24 16:13:29
  **/
public interface DictService {
	

   /**
     * 新增
     * @param dict 实体
     * @return
     */
    Dict addDict(Dict dict);

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
    void editDictById(Dict dict);

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
    void delDictById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    void delDictByIds(List<String> ids);

    /**
     * 获取所有(分页)
     * @param start 开始记录
     * @param pageSize 分页大小
     * @param map 参数
     * @return
     */
    PageInfo<Map<String, Object>> getDicts(int start, int pageSize, Map<String, Object> map);

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getDicts(Map<String, Object> map);
}
