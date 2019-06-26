package com.javaweb.MichaelKai.controller.app;

import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.enums.JwtResultEnums;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.utils.JwtUtil;
import com.javaweb.MichaelKai.common.utils.RedisUtil;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.jwt.annotation.IgnorePermissions;
import com.javaweb.MichaelKai.jwt.annotation.JwtPermissions;
import com.javaweb.MichaelKai.jwt.config.properties.JwtProperties;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: app端登录验证controller
 * @author: YuKai Fan
 * @create: 2019-06-24 11:56
 **/
@RestController
@RequestMapping("/app/api")
public class AppAuthController {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * app端用户登录
     * @param account
     * @param pwd
     * @return
     */
    @IgnorePermissions
    @PostMapping("/auth")
    public Result auth(String account, String pwd) {
        //根据账号获取系统用户数据
        Map<String, Object> map = new HashMap<>();
        map.put("account", account);
        List<Map<String, Object>> users = userService.getUsers(map);
        if (users.size() <= 0 || users.size() > 1) {
            throw new ResultException(JwtResultEnums.AUTH_REQUEST_ERROR.getCode(), JwtResultEnums.AUTH_REQUEST_ERROR.getMessage());
        } else if (users.get(0).get("status").equals(StatusEnum.Disable)) {
            throw new ResultException(JwtResultEnums.AUTH_REQUEST_LOCKED.getCode(), JwtResultEnums.AUTH_REQUEST_LOCKED.getMessage());
        } else {
            if (ShiroKit.md5(pwd, account + users.get(0).get("salt")).equals(users.get(0).get("password"))) {

                String token = JwtUtil.getToken(account, jwtProperties.getSecret(), jwtProperties.getExpired());
                return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), token);
            } else {
                throw new ResultException(JwtResultEnums.AUTH_REQUEST_ERROR.getCode(), JwtResultEnums.AUTH_REQUEST_ERROR.getMessage());
            }
        }
    }

    /**
     * app端用户注销
     * @param account
     * @return
     */
    @IgnorePermissions
    @PostMapping("/appLogout")
    public Result appLogout(String account, String token) {
        //先吧redis中的token删除
        redisUtil.del(Constant.JWT_TOKEN + account);

        //在redis中创建token 的黑名单，每个用户注销token都放入黑名单中
        redisUtil.set(Constant.JWT_TOKEN_BLACKLIST + token, token);
        return new Result(true, JwtResultEnums.TOKEN_LOGOUT.getCode(), JwtResultEnums.TOKEN_LOGOUT.getMessage());
    }

    @JwtPermissions
    @GetMapping("/test")
    public Result test() {
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), "测试成功");
    }
}