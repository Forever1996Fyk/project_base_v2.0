package com.javaweb.MichaelKai.activiti.service.customize.entity;

import com.google.common.collect.Lists;
import com.javaweb.MichaelKai.activiti.util.ActivitiUserUtils;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.mapper.UserMapper;
import com.javaweb.MichaelKai.pojo.Role;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 自定义group管理类
 * @author: YuKai Fan
 * @create: 2019-08-06 16:08
 **/
@Component
@Slf4j
public class CustomGroupEntityManager extends UserEntityManager {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Group> findGroupsByUser(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }

        List<Group> groups = Lists.newArrayList();
        List<Map<String, Object>> allRolesByUserId = userMapper.getAllRolesByUserId(userId);
        List<Role> roles = Lists.newArrayList();

        for (Map<String, Object> map : allRolesByUserId) {
            try {
                Role role = (Role) MapUtil.mapToObject(Role.class, map);
                roles.add(role);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("map转为实体错误, map = {}", map);
            }
        }

        if (!CollectionUtils.isEmpty(roles)) {
            groups = ActivitiUserUtils.toActivitiGroups(roles);
        }
        return groups;
    }
}