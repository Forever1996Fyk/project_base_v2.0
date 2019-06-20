package com.javaweb.MichaelKai.controller.login;

import com.javaweb.MichaelKai.annotation.ActionLog;
import com.javaweb.MichaelKai.common.enums.LogTypeEnum;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ExceptionHandle;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.properties.ProjectProperties;
import com.javaweb.MichaelKai.common.utils.CaptchaUtil;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.common.utils.HttpServletUtil;
import com.javaweb.MichaelKai.common.utils.SpringContextUtil;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.elfinder.ElFinderConstants;
import com.javaweb.MichaelKai.elfinder.command.CommandFactory;
import com.javaweb.MichaelKai.elfinder.configuration.ElFinderConfig;
import com.javaweb.MichaelKai.elfinder.configuration.ElfinderConfiguration;
import com.javaweb.MichaelKai.elfinder.core.Volume;
import com.javaweb.MichaelKai.elfinder.core.VolumeSecurity;
import com.javaweb.MichaelKai.elfinder.core.impl.DefaultVolumeSecurity;
import com.javaweb.MichaelKai.elfinder.core.impl.SecurityConstraint;
import com.javaweb.MichaelKai.elfinder.param.Node;
import com.javaweb.MichaelKai.elfinder.service.ElfinderStorage;
import com.javaweb.MichaelKai.elfinder.service.ElfinderStorageFactory;
import com.javaweb.MichaelKai.elfinder.service.VolumeSources;
import com.javaweb.MichaelKai.elfinder.service.impl.DefaultElfinderStorage;
import com.javaweb.MichaelKai.elfinder.service.impl.DefaultElfinderStorageFactory;
import com.javaweb.MichaelKai.elfinder.service.impl.DefaultThumbnailWidth;
import com.javaweb.MichaelKai.elfinder.support.locale.LocaleUtils;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import com.javaweb.MichaelKai.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @program: project_base
 * @description: 登录controller
 * @author: YuKai Fan
 * @create: 2019-05-27 11:19
 **/
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String toLogin(Model model) {
        ProjectProperties properties = SpringContextUtil.getBean(ProjectProperties.class);
        model.addAttribute("isCaptcha", properties.isCaptchaOpen());
        return "login";
    }


    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    @ActionLog(name = "用户登录", LOG_TYPE_ENUM = LogTypeEnum.USER_LOGIN)
    public Result login(@RequestBody UserVo user) {
        if (StringUtils.isEmpty(user.getAccount()) || StringUtils.isEmpty(user.getPassword())) {
            throw new ResultException(ResultEnum.USER_NAME_PWD_NULL);
        }

        //判断验证码 todo
        ProjectProperties properties = SpringContextUtil.getBean(ProjectProperties.class);
        if (properties.isCaptchaOpen()) {
            if (!CaptchaUtil.checkCaptcha(user.getCaptchaCode())) {
                throw new ResultException(ResultEnum.USER_CAPTCHA_ERROR.getValue(), ResultEnum.USER_CAPTCHA_ERROR.getMessage());
            }
        }

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

            //创建云盘上传文件

            return new Result(true, ResultEnum.SUCCESS.getValue(), "登录" + ResultEnum.SUCCESS.getMessage(), "/index");
        } catch (LockedAccountException e) {
            throw new ResultException(ResultEnum.ACCOUNT_LOCKED.getValue(), ResultEnum.ACCOUNT_LOCKED.getMessage());
        } catch (AuthenticationException e) {
            throw new ResultException(ResultEnum.ACCOUNT_PWD_ERROR.getValue(), ResultEnum.ACCOUNT_PWD_ERROR.getMessage());
        }
    }

    /**
     * 获取验证码图片
     * @param request
     * @param response
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置响应头信息,通知浏览器不要缓存
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "-1");
        response.setContentType("image/jpeg");

        //获取验证码
        String captcha = CaptchaUtil.getRandomCode();
        //将验证码存入session
        request.getSession().setAttribute("captcha", captcha);
        //输出到页面
        ImageIO.write(CaptchaUtil.genCaptcha(captcha), "jpg", response.getOutputStream());
    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }
}