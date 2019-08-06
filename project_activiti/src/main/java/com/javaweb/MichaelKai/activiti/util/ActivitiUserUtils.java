package com.javaweb.MichaelKai.activiti.util;

import com.google.common.collect.Lists;
import com.javaweb.MichaelKai.pojo.Role;
import com.javaweb.MichaelKai.pojo.User;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import java.util.List;

/**
 * @program: project_base
 * @description: 转化为activiti中使用的user
 * @author: YuKai Fan
 * @create: 2019-08-06 16:12
 **/
public class ActivitiUserUtils {

    /**
     * 将自己的user类，转为activiti的userEntity
     * @param user
     * @return
     */
    public static UserEntity toActivitiUser(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setFirstName(user.getUserName());
        userEntity.setLastName(user.getNickName());
        userEntity.setPassword(user.getPassword());
        userEntity.setEmail(user.getEmail());
        userEntity.setRevision(1);
        return userEntity;
    }

    /**
     * 将自己的role类，转为activiti的groupEntity
     * @param role
     * @return
     */
    public static GroupEntity toActivitiGroup(Role role) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setRevision(1);
        groupEntity.setType("assignment");
        groupEntity.setId(role.getId());
        groupEntity.setName(role.getRoleName());
        return groupEntity;
    }

    /**
     * 转为用户组集合
     * @param roles
     * @return
     */
    public static List<Group> toActivitiGroups(List<Role> roles) {
        List<Group> groups = Lists.newArrayList();
        for (Role role : roles) {
            GroupEntity groupEntity = toActivitiGroup(role);
            groups.add(groupEntity);
        }

        return groups;
    }
}