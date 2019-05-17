package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.common.utils.IdWorker;
import com.javaweb.MichaelKai.mapper.UserMapper;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: userService实现层
 * @author: YuKai Fan
 * @create: 2019-05-17 14:56
 **/
@Service
//autowired或报错，但是项目功能没错
@SuppressWarnings("SpringJavaAutowiringInspection")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdWorker idWorker;

    @Override
    public User addUser(User user) {
        user.setId(String.valueOf(idWorker.nextId()));
        userMapper.addUser(user);
        return user;
    }

    @Override
    public Map<String, Object> getUserById(String id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void editUserById(User user) {
        userMapper.editUserById(user);
    }

    @Override
    public void delUserById(String id) {
        userMapper.delUserById(id);
    }

    @Override
    public void delUserByIds(List<String> ids) {
        userMapper.delUserByIds(ids);
    }

    @Override
    public List<Map<String, Object>> getUsers(int start, int pageSize, Map<String, Object> map) {
        PageHelper.offsetPage(start, pageSize);
        return this.getUsers(map);
    }

    @Override
    public List<Map<String, Object>> getUsers(Map<String, Object> map) {
        return userMapper.getUsers(map);
    }
}