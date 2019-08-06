package com.javaweb.MichaelKai.activiti.service.customize.entity;

import com.google.common.collect.Lists;
import com.javaweb.MichaelKai.activiti.util.ActivitiUserUtils;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.mapper.UserMapper;
import com.javaweb.MichaelKai.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 自定义用户管理类
 * @author: YuKai Fan
 * @create: 2019-08-06 16:08
 **/
@Component
@Slf4j
public class CustomUserEntityManager extends UserEntityManager {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserEntity findUserById(String userId) {
        User user = getUserById(userId);
        return ActivitiUserUtils.toActivitiUser(user);
    }


    @Override
    public List<org.activiti.engine.identity.User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        User user = getUserById(query.getId());
        List<org.activiti.engine.identity.User> list = Lists.newArrayList();
        list.add(ActivitiUserUtils.toActivitiUser(user));
        return list;
    }

    public List<org.activiti.engine.identity.User> findUserByQueryCriteria() {
        List<User> users = getUsers();
        List<org.activiti.engine.identity.User> list = Lists.newArrayList();
        for (User user : users) {
            list.add(ActivitiUserUtils.toActivitiUser(user));
        }

        return list;
    }

    private List<User> getUsers() {
        List<Map<String, Object>> users = userMapper.getUsers(null);
        List<User> userList = Lists.newArrayList();
        for (Map<String, Object> map : users) {
            try {
                User user = (User) MapUtil.mapToObject(User.class, map);
                userList.add(user);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Map转为User失败, map = {}", map);
            }
        }

        return userList;
    }

    private User getUserById(String userId) {
        User user = new User();
        Map<String, Object> userById = userMapper.getUserById(userId);
        try {
            user = (User) MapUtil.mapToObject(User.class, userById);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("User转为UserEntity失败, userById = {}", userById);
        }

        return user;
    }
}