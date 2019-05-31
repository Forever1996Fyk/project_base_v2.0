package com.javaweb.MichaelKai.controller;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.annotation.ActionLog;
import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.enums.LogTypeEnum;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.utils.BeanUtil;
import com.javaweb.MichaelKai.common.utils.MD5Util;
import com.javaweb.MichaelKai.common.utils.PinYinUtil;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.pojo.UserRole;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import jdk.nashorn.internal.ir.IfNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 用户controller层
 * @author: YuKai Fan
 * @create: 2019-05-17 14:58
 **/
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 添加用户
     * @param user
     * @returneducation
     */
    @PostMapping("/user")
    @ActionLog(name = "用户新增", LOG_TYPE_ENUM = LogTypeEnum.USER_ADD)
    public Result addUser(@RequestBody User user) {
        //生成随机盐
        String salt = MD5Util.createSalt();
        String pwdMD5;
        if (StringUtils.isEmpty(user.getAccount())) {//如果账号为空,则设置默认账号为用户名拼音
            user.setAccount(PinYinUtil.toPinyin(user.getUserName()));
        }
        if (StringUtils.isEmpty(user.getPassword())) {//密码为空表示管理员创建用户,默认密码为111111
            pwdMD5 = Constant.DEFAULT_PWD;
        } else {
            pwdMD5 = user.getPassword();
        }

        user.setPassword(ShiroKit.md5(pwdMD5, user.getAccount() + salt));
        user.setSalt(salt);

        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), userService.addUser(user));
    }

    /**
     * 编辑修改用户
     * @param user
     * @return
     */
    @PutMapping("/user")
    @ActionLog(name = "用户更新", LOG_TYPE_ENUM = LogTypeEnum.USER_EDIT)
    public Result editUserById(@RequestBody User user) {
        userService.editUserById(user);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), user);
    }

    /**
     * 编辑修改个人
     * @param user
     * @return
     */
    @PutMapping("/userInfo")
    @ActionLog(name = "用户修改个人信息", LOG_TYPE_ENUM = LogTypeEnum.USER_EDIT)
    public Result editUserInfo(@RequestBody User user) {
        userService.editUserById(user);

        //更新缓存用户信息
        User subject = ShiroKit.getUser();
        BeanUtils.copyProperties(user, subject, BeanUtil.getNullPropNames(user));
        ShiroKit.resetCookieRememberMe();
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), user);
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/user")
    @ActionLog(name = "删除用户", LOG_TYPE_ENUM = LogTypeEnum.USER_DEL)
    public Result editUserById(@RequestParam String id) {
        userService.delUserById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id删除用户
     * @param ids
     * @return
     */
    @DeleteMapping("/users/{ids}")
    @ActionLog(name = "删除用户", LOG_TYPE_ENUM = LogTypeEnum.USER_DEL)
    public Result editUserByIds(@PathVariable("ids") String[] ids) {
        userService.delUserByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有的用户(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getUsers/noPage")
    public Result getUsers(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = userService.getUsers(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有用户
     * @param page 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getUsers")
    public Result getUsers(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                           @RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
                           @RequestParam Map<String, Object> map) {
        PageInfo<Map<String, Object>> pageList = userService.getUsers(page, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * 分配角色
     * @param userRole
     * @return
     */
    @PostMapping("/user/roleAssign")
    public Result roleAssign(@RequestBody UserRole userRole) {
        userService.roleAssign(userRole);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 上传用户头像
     * @param request
     * @return
     */
    @PostMapping("/uploadUserIcon")
    public Result uploadUserIcon(HttpServletRequest request) {
        User user = userService.uploadUserIcon(request, ShiroKit.getUser().getId());
        if (user != null) {
            User subject = ShiroKit.getUser();
            subject.setUserIcon(user.getUserIcon());
            ShiroKit.resetCookieRememberMe();
        }
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), user);
    }

}