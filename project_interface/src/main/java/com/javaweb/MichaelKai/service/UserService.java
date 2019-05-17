package com.javaweb.MichaelKai.service;

import com.javaweb.MichaelKai.pojo.User;

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
    List<Map<String, Object>> getUsers(int start, int pageSize, Map<String, Object> map);

    /**
     * 获取所有用户
     * @param map 参数
     * @return
     */
    List<Map<String, Object>> getUsers(Map<String, Object> map);

}