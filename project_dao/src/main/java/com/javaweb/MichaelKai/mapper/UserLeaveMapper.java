package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;
import com.javaweb.MichaelKai.pojo.UserLeave;
import org.apache.ibatis.annotations.Param;


/**
 * 请假流程表
 * 
 * @author YuKai Fan
 * @date 2019-08-08 10:30:23
 */
public interface UserLeaveMapper {



    /**
     * 新增
     * @param userLeave 实体
     * @return
     */
    int addUserLeave(UserLeave userLeave);

    /**
     * 批量新增
     * @param list 实体集合
     */
    void addUserLeaves(@Param(value = "list") List<UserLeave> list);

    /**
     * 根据id查询
     * @param id  主键
     * @return
     */
    Map<String, Object> getUserLeaveById(String id);

    /**
     * 根据id修改
     * @param userLeave 实体
     * @return
     */
    int editUserLeaveById(UserLeave userLeave);

    /**
     * 批量修改
     *
     * @param userLeave 实体
     * @param ids 主键集合
     */
    void editUserLeaveByIds(@Param("map") UserLeave userLeave, @Param("list") List<String> ids);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int delUserLeaveById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delUserLeaveByIds(List<String> ids);

    /**
     * 真删除
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delUserLeaveRealById(String id);

    /**
     * 真批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delUserLeaveRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delUserLeaveReals();

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getUserLeaves(Map<String, Object> map);

}
