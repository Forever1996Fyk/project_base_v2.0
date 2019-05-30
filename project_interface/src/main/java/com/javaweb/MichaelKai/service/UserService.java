package com.javaweb.MichaelKai.service;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.pojo.Role;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.pojo.UserRole;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 用户service层
 * @author: YuKai Fan
 * @create: 2019-05-17 13:48
 **/
public interface UserService {

    /**
     * 新增用户
     * @param user
     * @return
     */
    User addUser(User user);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    Map<String, Object> getUserById(String id);

    /**
     * 根据id修改用户
     * @param user
     */
    void editUserById(User user);

    /**
     * 批量修改
     *
     * @param role 实体
     * @param ids 主键集合
     */
    void editUserByIds(@Param("map") Role role, @Param("list") List<String> ids);

    /**
     * 根据id删除用户(软删除)
     * @param id
     */
    void delUserById(String id);

    /**
     * 批量删除
     * @param ids
     */
    void delUserByIds(List<String> ids);

    /**
     * 获取所有用户(分页)
     * @param start 开始记录
     * @param pageSize 分页大小
     * @param map 参数
     * @return
     */
    PageInfo<Map<String, Object>> getUsers(int start, int pageSize, Map<String, Object> map);

    /**
     * 获取所有用户
     * @param map 参数
     * @return
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

    /**
     * 角色分配
     * @param userRole
     */
    void roleAssign(UserRole userRole);

    /**
     * 验证用户信息是否已经存在
     * @param user
     * @return
     */
    Map<String, Object> checkUser(User user);

    /**
     *
     * @param request
     */
    User uploadUserIcon(HttpServletRequest request, String userId);
}