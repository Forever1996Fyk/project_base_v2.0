package com.javaweb.MichaelKai.service;

import com.javaweb.MichaelKai.pojo.UserLeave;
import org.apache.ibatis.annotations.Param;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

 /**
  * @program: project_base
  * @description: 请假流程表
  * @author: YuKai Fan
  * @create: 2019-08-08 10:30:23
  **/
public interface UserLeaveService {
	

   /**
     * 新增
     * @param userLeave 实体
     * @return
     */
    UserLeave addUserLeave(UserLeave userLeave);

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
    void editUserLeaveById(UserLeave userLeave);

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
    void delUserLeaveById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    void delUserLeaveByIds(List<String> ids);

    /**
     * 获取所有(分页)
     * @param start 开始记录
     * @param pageSize 分页大小
     * @param map 参数
     * @return
     */
    PageInfo<Map<String, Object>> getUserLeaves(int start, int pageSize, Map<String, Object> map);

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getUserLeaves(Map<String, Object> map);
}
