package com.javaweb.MichaelKai.controller.login;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ExceptionHandle;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.properties.ProjectProperties;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.common.utils.HttpServletUtil;
import com.javaweb.MichaelKai.common.utils.SpringContextUtil;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import com.javaweb.MichaelKai.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @program: project_base
 * @description: 登录controller
 * @author: YuKai Fan
 * @create: 2019-05-27 11:19
 **/
@Controller
public class LoginController {

    @Autowired
    private ExceptionHandle exceptionHandle;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String toLogin(Model model) {
        ProjectProperties properties = SpringContextUtil.getBean(ProjectProperties.class);
        model.addAttribute("isCaptcha", properties.isCaptchOpen());
        return "login";
    }


    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestBody UserVo user) {
        if (StringUtils.isEmpty(user.getAccount()) || StringUtils.isEmpty(user.getPassword())) {
            throw new ResultException(ResultEnum.USER_NAME_PWD_NULL);
        }

        //判断验证码 todo

        //1.获取主体对象subject
        Subject subject = SecurityUtils.getSubject();

        //2. 封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(user.getAccount(), user.getPassword());

        //3. 执行登录,自定义realm类
        try {
            subject.login(token);

            //登录成功修改最后登录时间
            User loginUser = ShiroKit.getUser();
            loginUser.setLastLoginTime(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            userService.editUserById(loginUser);

            return new Result(true, ResultEnum.SUCCESS.getValue(), "登录" + ResultEnum.SUCCESS.getMessage(), "/index");
        } catch (LockedAccountException e) {
            return exceptionHandle.exception(e);
        } catch (AuthenticationException e) {
            return exceptionHandle.exception(e);
        }
    }
}