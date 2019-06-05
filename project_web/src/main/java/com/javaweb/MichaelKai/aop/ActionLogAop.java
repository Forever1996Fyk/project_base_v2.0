package com.javaweb.MichaelKai.aop;

import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.enums.LogTypeEnum;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.pojo.ActionLog;
import com.javaweb.MichaelKai.service.ActionLogService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @program: project_base
 * @description: 用户操作日志aop
 * @author: YuKai Fan
 * @create: 2019-05-31 16:37
 **/
@Aspect
@Component
public class ActionLogAop {
    @Autowired
    private ActionLogService actionLogService;

    /**
     * 拦截所有使用@ActionLog注解的接口
     */
    @Pointcut("@annotation(com.javaweb.MichaelKai.annotation.ActionLog)")
    private void actionLog(){}

    @Around("actionLog()")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //执行切入点，获取返回值
        Object proceed = joinPoint.proceed();

        //读取ActionLog注解信息
        Method targetMethod = ((MethodSignature)joinPoint.getSignature()).getMethod();
        com.javaweb.MichaelKai.annotation.ActionLog anno =
                targetMethod.getAnnotation(com.javaweb.MichaelKai.annotation.ActionLog.class);
        //获取name值
        String name = anno.name();
        //获取type值
        LogTypeEnum logTypeEnum = anno.LOG_TYPE_ENUM();
        //当前时间
        String curTime = DateUtil.dateToString(new Date(), Constant.DATE_FORMAT_CREATE_UPDATE);
        //当前登录人
        String userId = ShiroKit.getUser().getId();

        //封装日志实例对象
        ActionLog actionLog = new ActionLog();
        actionLog.setName(name.isEmpty() ? "用户操作" : name);
        actionLog.setType(logTypeEnum.getType());
        actionLog.setMethod(targetMethod.getName());
        actionLog.setMessage(logTypeEnum.getMessage());
        actionLog.setIpaddr(ShiroKit.getIp());
        actionLog.setClazz(joinPoint.getTarget().getClass().getName());
        actionLog.setCreateUserId(userId);
        actionLog.setCreateTime(curTime);
        actionLog.setUpdateTime(curTime);
        actionLog.setUpdateUserId(userId);

        actionLogService.addActionLog(actionLog);

        return proceed;
    }
}