package com.javaweb.MichaelKai.common.utils;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;

import java.util.Date;

/**
 * @program: project_base
 * @description: cron工具类
 * @author: YuKai Fan
 * @create: 2019-06-28 11:55
 **/
public class CronUtil {

    /**
     * 上次执行时间
     * @param cron
     * @param jobName
     * @param jobGroup
     * @return
     */
    public static long getLastTriggerTime(String cron, String jobName, String jobGroup) {
        if (!CronExpression.isValidExpression(cron)) {
            return 0;
        }
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(cronScheduleBuilder).build();
        Date time0 = cronTrigger.getStartTime();
        Date time1 = cronTrigger.getFireTimeAfter(time0);
        Date time2 = cronTrigger.getFireTimeAfter(time1);
        Date time3 = cronTrigger.getFireTimeAfter(time2);
        long l = time1.getTime() - (time3.getTime() - time2.getTime());
        return l;
    }

    /**
     * 获取下次执行时间(getFireTimeAfter,也可以获取下下次)
     * @param cron
     * @param jobName
     * @param jobGroup
     * @return
     */
    public static long getNextTriggerTime(String cron, String jobName, String jobGroup) {
        if (!CronExpression.isValidExpression(cron)) {
            return 0;
        }

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(cronScheduleBuilder).build();
        Date time0 = cronTrigger.getStartTime();
        Date time1 = cronTrigger.getFireTimeAfter(time0);

        return time1.getTime();
    }
}