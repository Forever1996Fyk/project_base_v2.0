package com.javaweb.MichaelKai.aop;

import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.Role;
import com.javaweb.MichaelKai.service.RoleService;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.vo.UserRoleVo;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * @program: project_base
 * @description: 切入时间系统用户 角色 用户-角色 同步到Activiti 用户 组 用户组 同步到工作流
 * @author: YuKai Fan
 * @create: 2019-08-07 14:12
 **/
@Aspect
@Component
public class ListenUserRoleAop {
    @Autowired
    private IdentityService identityService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /******************************监听用户处理begin****************************/

    /**
     * 监听 新增用户 同步工作流用户表 环绕注解能得到插入用户的id
     * @param joinPoint
     * @return
     */
    @Around("execution(com.javaweb.MichaelKai.common.vo.Result com.javaweb.MichaelKai.controller.UserController.addUser(*))")
    public Object listenerAddUser(ProceedingJoinPoint joinPoint) throws Throwable {
        return getObject(joinPoint);
    }

    /**
     * 监听 角色分配 同步工作流用户组关联表
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(com.javaweb.MichaelKai.common.vo.Result com.javaweb.MichaelKai.controller.UserController.roleAssign(*))")
    public Object listenerRoleAssign(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object o = joinPoint.proceed(args);
        Result result = (Result) o;
        if (result.isFlag()) {
            synchronizeActivitiUserGroup(args);
        }

        return o;
     }

    /**
     * 监听 修改用户 同步工作流用户表
     * @param joinPoint
     * @return
     */
    @Around("execution(com.javaweb.MichaelKai.common.vo.Result com.javaweb.MichaelKai.controller.UserController.editUserById(*))")
    public Object listenerEditUser(ProceedingJoinPoint joinPoint) throws Throwable {

        return getObject(joinPoint);
    }

    /**
     * 监听 删除用户 同步工作流用户表
     * @param joinPoint
     * @return
     */
    @Around("execution(com.javaweb.MichaelKai.common.vo.Result com.javaweb.MichaelKai.controller.UserController.deleteUserById(*))")
    public Object listenerDelUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object o = joinPoint.proceed(args);
        Result result = (Result) o;
        if (result.isFlag()) {
            identityService.deleteUser((String) args[0]);
        }

        return o;
    }

    /**
     * 监听 批量删除用户 同步工作流用户表
     * @param joinPoint
     * @return
     */
    @Around("execution(com.javaweb.MichaelKai.common.vo.Result com.javaweb.MichaelKai.controller.UserController.deleteUserByIds(*))")
    public Object listenerDelUsers(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object o = joinPoint.proceed(args);
        Result result = (Result) o;
        if (result.isFlag()) {
            String[] ids = (String[]) args[0];
            for (String id : ids) {
                identityService.deleteUser(id);
            }
        }

        return o;
    }

    /******************************监听用户处理end****************************/


    /******************************监听角色处理begin****************************/

    /**
     * 监听 新增角色 同步工作流用户组表
     * @param joinPoint
     * @return
     */
    @Around("execution(com.javaweb.MichaelKai.common.vo.Result com.javaweb.MichaelKai.controller.RoleController.addRole(*))")
    public Object listenerAddRole(ProceedingJoinPoint joinPoint) throws Throwable {
        return getRoleObject(joinPoint);
    }

    /**
     * 监听 修改角色 同步工作流用户组表
     * @param joinPoint
     * @return
     */
    @Around("execution(com.javaweb.MichaelKai.common.vo.Result com.javaweb.MichaelKai.controller.RoleController.editRoleById(*))")
    public Object listenerEditRole(ProceedingJoinPoint joinPoint) throws Throwable {
        return getRoleObject(joinPoint);
    }

    /**
     * 监听 删除角色 同步工作流用户组表
     * @param joinPoint
     * @return
     */
    @Around("execution(com.javaweb.MichaelKai.common.vo.Result com.javaweb.MichaelKai.controller.RoleController.deleteRoleById(*))")
    public Object listenerDelRole(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object o = joinPoint.proceed(args);
        Result result = (Result) o;
        if (result.isFlag()) {
            identityService.deleteGroup((String) args[0]);
        }
        return getRoleObject(joinPoint);
    }

    /**
     * 监听 批量删除角色 同步工作流用户组表
     * @param joinPoint
     * @return
     */
    @Around("execution(com.javaweb.MichaelKai.common.vo.Result com.javaweb.MichaelKai.controller.RoleController.deleteRoleByIds(*))")
    public Object listenerDelRoles(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object o = joinPoint.proceed(args);
        Result result = (Result) o;
        if (result.isFlag()) {
            String[] ids = (String[]) args[0];
            for (String id : ids) {
                identityService.deleteGroup(id);
            }
        }
        return getRoleObject(joinPoint);
    }

    /******************************监听角色处理end****************************/


    private Object getRoleObject(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object o = joinPoint.proceed(args);
        Result result = (Result) o;
        if (result.isFlag()) {
            synchronizeActivitiGroup(args);
        }
        return o;
    }


    private Object getObject(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object o = joinPoint.proceed(args);
        Result result = (Result) o;
        if (result.isFlag()) {
            synchronizeActivitiUser(args);
        }
        return o;
    }

    /**
     * 同步进activiti用户组
     * @param obj
     */
    private void synchronizeActivitiUserGroup(Object[] obj) {
        UserRoleVo userRoleVo = (UserRoleVo) obj[0];

        //删除用户-组关联
        String[] roleIds = userRoleVo.getRoleId().split(",");
        for (String roleId : roleIds) {
            identityService.deleteMembership(userRoleVo.getUserId(), roleId);
        }

        //重新关联
        if (!CollectionUtils.isEmpty(Arrays.asList(roleIds))) {
            for (String roleId : roleIds) {
                identityService.createMembership(userRoleVo.getUserId(), roleId);
            }
        }
    }

    /**
     * 同步activiti用户
     * @param obj
     */
    private void synchronizeActivitiUser(Object[] obj) throws Exception {
        com.javaweb.MichaelKai.pojo.User user = (com.javaweb.MichaelKai.pojo.User) obj[0];
        identityService.deleteUser(user.getId());

        User au = new UserEntity();

        Map<String, Object> userById = userService.getUserById(user.getId());
        if (CollectionUtils.isEmpty(userById)) {
            au.setId(user.getId());
            au.setFirstName(user.getUserName());
            au.setLastName(user.getNickName());
            au.setPassword(user.getPassword());
            au.setEmail(user.getEmail());

        } else {
            com.javaweb.MichaelKai.pojo.User user1 =
                    (com.javaweb.MichaelKai.pojo.User) MapUtil.mapToObject(com.javaweb.MichaelKai.pojo.User.class, userById);
            au.setId(user1.getId());
            au.setFirstName(user1.getUserName());
            au.setLastName(user1.getNickName());
            au.setEmail(user1.getEmail());
            au.setPassword(user1.getPassword());
        }

        identityService.saveUser(au);


    }

    /**
     * 同步activiti用户组
     * @param obj
     */
    private void synchronizeActivitiGroup(Object[] obj) throws Exception {
        Role role = (Role) obj[0];
        identityService.deleteGroup(role.getId());

        Group group = new GroupEntity();

        Map<String, Object> roleById = roleService.getRoleById(role.getId());
        if (CollectionUtils.isEmpty(roleById)) {
            group.setId(role.getId());
            group.setName(role.getRoleName());
        } else {
            role = (Role) MapUtil.mapToObject(Role.class, roleById);
            group.setId(role.getId());
            group.setName(role.getRoleName());
        }

        identityService.saveGroup(group);
    }
}