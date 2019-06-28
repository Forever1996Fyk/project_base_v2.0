package com.javaweb.MichaelKai.quartz;

import com.javaweb.MichaelKai.pojo.ScheduleJob;
import com.javaweb.MichaelKai.quartz.service.QuartzService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @program: project_base
 * @description: quartz任务工厂类
 * @author: YuKai Fan
 * @create: 2019-06-27 15:41
 **/
public class QuartzJobFactory extends QuartzJobBean {
    @Autowired
    private QuartzService quartzService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        if (scheduleJob.getMethodName() == null || "".equals(scheduleJob.getMethodName())) {
            quartzService.executeTask(scheduleJob.getBeanName());
        } else {
            quartzService.executeTask(scheduleJob.getBeanName(), scheduleJob.getMethodName());
        }
    }
}