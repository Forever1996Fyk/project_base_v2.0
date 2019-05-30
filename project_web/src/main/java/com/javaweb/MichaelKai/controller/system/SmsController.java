package com.javaweb.MichaelKai.controller.system;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: project_base
 * @description: 发送短信验证码controller
 * @author: YuKai Fan
 * @create: 2019-05-29 15:13
 **/
@RestController
@RequestMapping("/api")
public class SmsController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码(不需要验证手机号)
     * @param phone
     * @return
     */
    @GetMapping("/sendSmsCodeNotCheckPhone")
    public Result sendSmsCodeNotCheckPhone(String phone) {
        //发送验证码 todo

        return new Result(true, ResultEnum.SUCCESS.getValue(), "发送" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 发送验证码(需要验证手机号)
     * @param phone
     * @return
     */
    @GetMapping("/sendSmsCode")
    public Result sendSmsCode(String phone) {

        //验证手机号
        User user = new User();
        user.setPhone(phone);
        Map<String, Object> map = userService.checkUser(user);
        if (!Boolean.valueOf(map.get("flag").toString())) {//手机号存在
            //发送验证码 todo


            return new Result(true, ResultEnum.SUCCESS.getValue(), "发送" + ResultEnum.SUCCESS.getMessage());
        } else {
            return new Result(false, ResultEnum.USER_PHONE_NOT_EXIST.getValue(), ResultEnum.USER_PHONE_NOT_EXIST.getMessage());
        }

    }
}