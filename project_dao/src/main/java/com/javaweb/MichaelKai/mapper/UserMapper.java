package com.javaweb.MichaelKai.mapper;

import com.javaweb.MichaelKai.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: dao层接口
 * @author: YuKai Fan
 * @create: 2019-05-17 13:38
 **/

public interface UserMapper {

    /**
     * 新增用户
     * @param user 用户实体
     * @return
     */
    int addUser(User user);

    /**
     * 批量新增用户
     * @param list 用户集合
     */
    void addUsers(@Param(value = "list")List<User> list);

    /**
     * 根据id查询指定用户
     * @param id  主键
     * @return
     */
    Map<String, Object> getUserById(String id);

    /**
     * 根据id修改用户
     * @param user 用户实体
     * @return
     */
    int editUserById(User user);

    /**
     * 批量修改用户
     *
     * @param user 用户实体
     * @param ids 主键集合
     */
    void editUserByIds(@Param("map") User user, @Param("list") List<String> ids);

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    int delUserById(String id);

    /**
     * 批量删除用户
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delUserByIds(List<String> ids);

    /**
     * 真删除用户
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delUserRealById(String id);

    /**
     * 真批量删除用户
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delUserRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delUserReals();

    /**
     * 获取所有用户.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getUsers(Map<String, Object> map);

    /**
     * 根据userId获取所有的角色
     * @param userId
     * @return
     */
    List<Map<String, Object>> getAllRolesByUserId(String userId);

    /**
     * 根据userId获取所有的权限
     * @param userId
     * @return
     */
    List<Map<String, Object>> getAllPermissionsByUserId(String userId);
}