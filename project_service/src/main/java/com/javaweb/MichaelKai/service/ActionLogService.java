package com.javaweb.MichaelKai.service;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.pojo.ActionLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 用户日志
 * @author: YuKai Fan
 * @create: 2019-05-31 15:39:53
 **/
public interface ActionLogService {


  /**
    * 新增
    * @param actionLog 实体
    * @return
    */
   ActionLog addActionLog(ActionLog actionLog);

   /**
    * 根据id查询
    * @param id  主键
    * @return
    */
   Map<String, Object> getActionLogById(String id);

   /**
    * 根据id修改
    * @param actionLog 实体
    * @return
    */
   void editActionLogById(ActionLog actionLog);

   /**
    * 批量修改
    *
    * @param actionLog 实体
    * @param ids 主键集合
    */
   void editActionLogByIds(@Param("map") ActionLog actionLog, @Param("list") List<String> ids);

   /**
    * 根据id删除
    * @param id
    * @return
    */
   void delActionLogById(String id);

   /**
    * 批量删除
    *
    * @param ids 主键集合
    * @return dao成功失败标志
    */
   void delActionLogByIds(List<String> ids);

   /**
    * 获取所有(分页)
    * @param start 开始记录
    * @param pageSize 分页大小
    * @param map 参数
    * @return
    */
   PageInfo<Map<String, Object>> getActionLogs(int start, int pageSize, Map<String, Object> map);

   /**
    * 获取所有数据.
    * @param map 页面表单
    * @return 结果集合
    */
   List<Map<String, Object>> getActionLogs(Map<String, Object> map);

   /**
    * 清空数据
    */
   void delActionLogReals();

 /**
  * 根据类型分组获取每月,每日,每年的日志总数
  * @param type
  * @return
  */
    List<Map<String,Object>> getLogGroupByType(String type);
}
