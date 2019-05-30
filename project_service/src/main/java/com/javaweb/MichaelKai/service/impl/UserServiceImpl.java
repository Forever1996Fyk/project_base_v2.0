package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.enums.FileTypeEnum;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.javaweb.MichaelKai.common.utils.IdWorker;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.mapper.UserMapper;
import com.javaweb.MichaelKai.pojo.Attachment;
import com.javaweb.MichaelKai.pojo.Role;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.pojo.UserRole;
import com.javaweb.MichaelKai.service.AttachmentService;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.thymeleaf.util.DictUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: userService实现层
 * @author: YuKai Fan
 * @create: 2019-05-17 14:56
 **/
@Service
@Transactional
//autowired或报错，但是项目功能没错
@SuppressWarnings("SpringJavaAutowiringInspection")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private AttachmentService attachmentService;

    @Override
    public User addUser(User user) {
        user.setId(String.valueOf(idWorker.nextId()));
        user.setStatus(StatusEnum.Normal.getValue());
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
    public void editUserByIds(Role role, List<String> ids) {

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
    public PageInfo<Map<String, Object>> getUsers(int start, int pageSize, Map<String, Object> map) {
        PageHelper.offsetPage(start, pageSize);
        List<Map<String, Object>> list = this.getUsers(map);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getUsers(Map<String, Object> map) {
        List<Map<String, Object>> users = userMapper.getUsers(map);
        for (Map<String, Object> user : users) {
            if (user.get("education") != null) {
                user.put("educationName", DictUtil.keyValue("EDUCATION_TYPE", user.get("education").toString()));
            }
            if (user.get("marryFlag") != null) {
                user.put("marryFlagName", DictUtil.keyValue("MARRAY_TYPE", user.get("marryFlag").toString()));
            }
            if (user.get("status") != null) {
                user.put("statusName", DictUtil.keyValue("STATUS_TYPE", user.get("status").toString()));
            }

        }
        return users;
    }

    @Override
    public List<Map<String, Object>> getAllRolesByUserId(String userId) {
        return userMapper.getAllRolesByUserId(userId);
    }

    @Override
    public List<Map<String, Object>> getAllPermissionsByUserId(String userId) {
        return userMapper.getAllPermissionsByUserId(userId);
    }

    @Override
    public void roleAssign(UserRole userRole) {
        //先删除表中的角色
        userMapper.delUserRole(userRole.getUserId());

        //在新增角色
        try {
            if (userRole.getRoleId() != null) {
                String[] roleIds = userRole.getRoleId().split(",");
                if (roleIds.length > 0) {
                    List<UserRole> list = new ArrayList<>();
                    for (String roleId : roleIds) {
                        UserRole uR = new UserRole();
                        uR.setUserId(userRole.getUserId());
                        uR.setRoleId(roleId);

                        list.add(uR);
                    }
                    userMapper.addUserRole(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> checkUser(User user) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(user.getAccount())) {
            if(!checkParam("account", user.getAccount())) {
                return returnResult(false, "账号已存在");
            }
        }

        if (StringUtils.isEmpty(user.getNickName())) {
            if (!checkParam("nickName", user.getNickName())) {
                return returnResult(false, "昵称已存在");
            }
        }

        if (StringUtils.isEmpty(user.getPhone())) {
            if (!checkParam("phone", user.getPhone())) {
                return returnResult(false, "手机号已存在");
            }
        }

        map.put("flag", true);
        return map;
    }

    @Override
    public User uploadUserIcon(HttpServletRequest request, String userId) {
        try {
            Attachment attachment = attachmentService.addAttachment(request, userId, FileTypeEnum.PIC.getAttachType());
            if (attachment != null) {
                User user = new User();
                user.setId(userId);
                user.setUserIcon(attachment.getId());
                userMapper.editUserById(user);
                return user;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private Boolean checkParam(String key, String val) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, val);
        switch (key) {
            case "account":
                return checkResult(map);
            case "nickName":
                return checkResult(map);
            case "phone":
                return checkResult(map);
        }
        return true;
    }

    //验证结果
    private Boolean checkResult(Map<String, Object> map) {
        List<Map<String, Object>> users = userMapper.getUsers(map);
        if (users.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    //返回结果
    private Map<String, Object> returnResult(boolean flag, String message) {
        Map<String, Object> map = new HashMap<>();
        if (flag) {
            map.put("flag", flag);
        } else {
            map.put("flag", flag);
            map.put("message", message);
        }
        return map;
    }


}