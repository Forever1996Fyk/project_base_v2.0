package com.javaweb.MichaelKai.quartz.service.impl;

import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.enums.QuartzEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import com.javaweb.MichaelKai.common.utils.CronUtil;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.mapper.ScheduleJobMapper;
import com.javaweb.MichaelKai.pojo.ScheduleJob;
import com.javaweb.MichaelKai.quartz.QuartzJobFactory;
import com.javaweb.MichaelKai.quartz.service.ScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-27 14:51
 **/
@Service
@Transactional
@Slf4j
public class ScheduleJobServiceImpl implements ScheduleJobService {
    @Autowired
    private ScheduleJobMapper scheduleJobMapper;
    @Autowired
    private Scheduler scheduler;

    @Override
    public List<ScheduleJob> getAllScheduleJobs() {
        List<ScheduleJob> jobList = new ArrayList<>();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyGroup();
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey key : jobKeys) {
                //获取触发器
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(key);
                for (Trigger trigger : triggers) {
                    ScheduleJob scheduleJob = getScheduleJob(scheduler, key, trigger);
                    //从cron中获取上次执行时间
                    String lastTriggerTime = DateUtil.longToString(CronUtil.getLastTriggerTime(scheduleJob.getCronExpression(), scheduleJob.getJobName(), scheduleJob.getJobGroup()), Constant.DATE_FORMAT_COMMON);
                    scheduleJob.setLastTriggerTime(lastTriggerTime);

                    //从cron中获取下次执行时间
                    String nextTriggerTime = DateUtil.longToString(CronUtil.getNextTriggerTime(scheduleJob.getCronExpression(), scheduleJob.getJobName(), scheduleJob.getJobGroup()), Constant.DATE_FORMAT_COMMON);
                    scheduleJob.setNextTriggerTime(nextTriggerTime);
                    jobList.add(scheduleJob);
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("[ScheduleJobServiceImpl] get the jobKeys is error:{}", e);
        }
        return jobList;
    }

    /**
     * 获取所有任务
     * @param scheduler
     * @param key
     * @param trigger
     * @return
     */
    private ScheduleJob getScheduleJob(Scheduler scheduler, JobKey key, Trigger trigger) {
        ScheduleJob scheduleJob = new ScheduleJob();
        try {
            JobDetail jobDetail = scheduler.getJobDetail(key);
            scheduleJob = (ScheduleJob) jobDetail.getJobDataMap().get("scheduleJob");
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

            scheduleJob.setJobStatus(triggerState.name());//获取任务状态
            scheduleJob.setJobGroup(key.getGroup());//获取任务组名称
            scheduleJob.setJobName(key.getName());//获取任务名称

            //如果是cron触发器
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                scheduleJob.setCronExpression(cronTrigger.getCronExpression());
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("[ScheduleJobServiceImpl] method getScheduleJob get JobDetail error:{}", e);
        }
        return scheduleJob;
    }

    @Override
    public void checkNotNull(ScheduleJob scheduleJob) {
        if (scheduleJob == null) {
            throw new IllegalStateException("scheduleJob is null,Please check it");
        }

        if (scheduleJob.getJobName() == null || "".equals(scheduleJob.getJobName())) {
            throw new IllegalStateException("the jobName of scheduleJob is null,Please check it");
        }

        if (scheduleJob.getJobGroup()==null || scheduleJob.getJobGroup().equals("")){
            throw new IllegalStateException("the jobGroup of scheduleJob is null,Please check it");
        }

        if (scheduleJob.getBeanName()==null || scheduleJob.getBeanName().equals("")){
            throw new IllegalStateException("the BeanName of scheduleJob is null,Please check it");
        }
    }

    @Override
    public List<ScheduleJob> getAllRunningJobs() throws SchedulerException {

        //获取正在执行的任务
        List<JobExecutionContext> executingJobList = scheduler.getCurrentlyExecutingJobs();
        List<ScheduleJob> jobList = new ArrayList<>();
        //遍历正在执行的任务，获取任务上下文信息
        for (JobExecutionContext jobExecutionContext : executingJobList) {
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            JobKey key = jobDetail.getKey();
            Trigger trigger = jobExecutionContext.getTrigger();

            ScheduleJob scheduleJob = getScheduleJob(scheduler, key, trigger);
            jobList.add(scheduleJob);
        }
        return jobList;
    }

    @Override
    public void addOrEditJob(ScheduleJob scheduleJob) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (cronTrigger == null) {
            addScheduleJob(scheduleJob);
        } else {
            editCronScheduleJob(scheduleJob);
        }

    }

    /**
     * 添加任务
     * @param scheduleJob
     * @throws Exception
     */
    private void addScheduleJob(ScheduleJob scheduleJob) throws Exception {
        checkNotNull(scheduleJob);
        if (StringUtils.isBlank(scheduleJob.getCronExpression())) {
            throw new ResultException(QuartzEnum.CRON_NOT_NULL.getCode(), QuartzEnum.CRON_NOT_NULL.getMessage());
        } else if (!CronExpression.isValidExpression(scheduleJob.getCronExpression())) {
            throw new ResultException(QuartzEnum.CRON_VALID_ERROR.getCode(), QuartzEnum.CRON_VALID_ERROR.getMessage());
        }

        //保存到数据库
        try {
            //创建jobName, jobGroup的jobDetail实例
            JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                    .build();
            jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
            //创建cron任务
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
            //创建cron触发器
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                    .withSchedule(cronScheduleBuilder.withMisfireHandlingInstructionDoNothing()).build();

            scheduler.scheduleJob(jobDetail, cronTrigger);

            String id = AppUtil.randomId();
            scheduleJob.setJobStatus("NORMAL");
            scheduleJob.setId(id);
            scheduleJob.setJobId(id);
            scheduleJobMapper.addScheduleJob(scheduleJob);
            log.info("[ScheduleJobServiceImpl] the scheduleJob is:{}", scheduleJob);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新编辑任务
     * @param scheduleJob
     * @throws Exception
     */
    private void editCronScheduleJob(ScheduleJob scheduleJob) throws Exception {
        checkNotNull(scheduleJob);
        if (StringUtils.isBlank(scheduleJob.getCronExpression())) {
            throw new Exception("[SchedulerJobServiceImpl] CronExpression不能为空");
        }
        //根据jobName与jobGroup获取触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
        //更新任务job
        scheduler.rescheduleJob(triggerKey, cronTrigger);

        //更新数据库
        scheduleJobMapper.editScheduleJobById(scheduleJob);
    }

    @Override
    public void pauseJob(Map<String, Object> map) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(map.get("jobName").toString(), map.get("jobGroup").toString());
        List<Map<String, Object>> scheduleJobs = scheduleJobMapper.getScheduleJobs(map);
        if (scheduleJobs.size() > 0 && scheduleJobs.size() == 1) {
            ScheduleJob scheduleJob = new ScheduleJob();
            scheduleJob.setId(scheduleJobs.get(0).get("id").toString());
            scheduleJob.setJobStatus("PAUSED");
            scheduleJobMapper.editScheduleJobById(scheduleJob);
            scheduler.pauseJob(jobKey);
        }
    }

    @Override
    public void deleteJob(Map<String, Object> map) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(map.get("jobName").toString(), map.get("jobGroup").toString());
        List<Map<String, Object>> scheduleJobs = scheduleJobMapper.getScheduleJobs(map);
        if (scheduleJobs.size() > 0 && scheduleJobs.size() == 1) {
            scheduleJobMapper.delScheduleJobRealById(scheduleJobs.get(0).get("id").toString());
            scheduler.deleteJob(jobKey);
        }
    }

    @Override
    public void runOneJob(Map<String, Object> map) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(map.get("jobName").toString(), map.get("jobGroup").toString());
        List<Map<String, Object>> scheduleJobs = scheduleJobMapper.getScheduleJobs(map);
        if (scheduleJobs.size() > 0 && scheduleJobs.size() == 1) {
            ScheduleJob scheduleJob = new ScheduleJob();
            scheduleJob.setJobStatus("NORMAL");
            scheduleJob.setId(scheduleJobs.get(0).get("id").toString());
            scheduleJobMapper.editScheduleJobById(scheduleJob);
            scheduler.triggerJob(jobKey);
        }
    }

    @Override
    public void resumeJob(Map<String, Object> map) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(map.get("jobName").toString(), map.get("jobGroup").toString());
        scheduler.resumeJob(jobKey);
    }

    @Override
    public Map<String, Object> getScheduleJobById(String id) {
        return scheduleJobMapper.getScheduleJobById(id);
    }

    @Override
    public List<Map<String, Object>> getScheduleJobs(Map<String, Object> map) {
        return scheduleJobMapper.getScheduleJobs(map);
    }

    @Override
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    @Override
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
       /* //获取所有运行中的任务
        List<ScheduleJob> runningJobs = getAllRunningJobs();
        if (runningJobs == null || runningJobs.size() <= 0) {
            return false;
        }

        for (ScheduleJob runningJob : runningJobs) {
            Map<String, Object> map = new HashMap<>();
            map.put("jobName", runningJob.getJobName());
            map.put("jobGroup", runningJob.getJobGroup());
            pauseJob(map);
        }
        return true;*/
    }
}