package com.javaweb.MichaelKai.controller.login;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.utils.CaptchaUtil;
import com.javaweb.MichaelKai.common.utils.MD5Util;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.common.utils.SpringContextUtil;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.controller.UserController;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 注册controller
 * @author: YuKai Fan
 * @create: 2019-05-28 08:56
 **/
@RestController
@RequestMapping("/api")
public class RegController {
    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @return
     */
    @PostMapping("/reg")
    public Result reg(@RequestBody Map<String, Object> form) {
        try {
            User user = MapUtil.mapToObject(User.class, form, false);

            //验证账号，手机号，昵称是否已存在
            Map<String, Object> map = userService.checkUser(user);
            if (!Boolean.valueOf(map.get("flag").toString())) {
                return new Result(false, ResultEnum.ERROR.getValue(), map.get("message").toString());
            }

            UserController userController = SpringContextUtil.getBean(UserController.class);
            userController.addUser(user);
            return new Result(true, ResultEnum.SUCCESS.getValue(), "注册" + ResultEnum.SUCCESS.getMessage() + ", 即将跳转到登录页");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, ResultEnum.ERROR.getValue(), "注册" + ResultEnum.ERROR.getMessage());
        }
    }

    /**
     * 忘记密码
     * @param form
     * @return
     */
    @PostMapping("/forget")
    public Result forget(@RequestBody Map<String, Object> form, Model model) {
        //验证图形验证码 todo
        if (form.get("captchaCode") != null) {
            if (!CaptchaUtil.checkCaptcha(form.get("captchaCode").toString())) {
                throw new ResultException(ResultEnum.USER_CAPTCHA_ERROR.getValue(), ResultEnum.USER_CAPTCHA_ERROR.getMessage());
            }
        }
        //验证短信验证码 todo

        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 重置密码
     * @param form
     * @return
     */
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody Map<String, Object> form) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", form.get("phone"));
        List<Map<String, Object>> users = userService.getUsers(map);
        if (users.size() > 0 && users.size() == 1) {
            User user = MapUtil.mapToObject(User.class, users.get(0), false);

            //获取新盐
            String salt = MD5Util.createSalt();
            //对新密码进行加密
            String password = ShiroKit.md5(form.get("password").toString(), user.getAccount() + salt);

            user.setSalt(salt);
            user.setPassword(password);

            //更新密码
            userService.editUserById(user);
            return new Result(true, ResultEnum.SUCCESS.getValue(), "重置" + ResultEnum.SUCCESS.getMessage() + ", 即将跳转到登录页");
        } else {
            return new Result(false, ResultEnum.USER_PHONE_NOT_EXIST.getValue(), ResultEnum.USER_PHONE_NOT_EXIST.getMessage());
        }
    }
}