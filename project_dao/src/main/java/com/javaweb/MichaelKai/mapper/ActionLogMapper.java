package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;
import com.javaweb.MichaelKai.pojo.ActionLog;
import org.apache.ibatis.annotations.Param;


/**
 * 用户日志
 * 
 * @author YuKai Fan
 * @date 2019-05-31 15:39:53
 */
public interface ActionLogMapper {



    /**
     * 新增
     * @param actionLog 实体
     * @return
     */
    int addActionLog(ActionLog actionLog);

    /**
     * 批量新增
     * @param list 实体集合
     */
    void addActionLogs(@Param(value = "list") List<ActionLog> list);

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
    int editActionLogById(ActionLog actionLog);

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
    int delActionLogById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delActionLogByIds(List<String> ids);

    /**
     * 真删除
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delActionLogRealById(String id);

    /**
     * 真批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delActionLogRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delActionLogReals();

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getActionLogs(Map<String, Object> map);

}
