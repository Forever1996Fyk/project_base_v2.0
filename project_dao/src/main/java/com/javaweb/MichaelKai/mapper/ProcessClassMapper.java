package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;
import com.javaweb.MichaelKai.pojo.ProcessClass;
import org.apache.ibatis.annotations.Param;


/**
 * 流程分类表
 * 
 * @author YuKai Fan
 * @date 2019-08-12 10:14:44
 */
public interface ProcessClassMapper {



    /**
     * 新增
     * @param processClass 实体
     * @return
     */
    int addProcessClass(ProcessClass processClass);

    /**
     * 批量新增
     * @param list 实体集合
     */
    void addProcessClasss(@Param(value = "list") List<ProcessClass> list);

    /**
     * 根据id查询
     * @param id  主键
     * @return
     */
    Map<String, Object> getProcessClassById(String id);

    /**
     * 根据id修改
     * @param processClass 实体
     * @return
     */
    int editProcessClassById(ProcessClass processClass);

    /**
     * 批量修改
     *
     * @param processClass 实体
     * @param ids 主键集合
     */
    void editProcessClassByIds(@Param("map") ProcessClass processClass, @Param("list") List<String> ids);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int delProcessClassById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delProcessClassByIds(List<String> ids);

    /**
     * 真删除
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delProcessClassRealById(String id);

    /**
     * 真批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delProcessClassRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delProcessClassReals();

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getProcessClasss(Map<String, Object> map);

}
