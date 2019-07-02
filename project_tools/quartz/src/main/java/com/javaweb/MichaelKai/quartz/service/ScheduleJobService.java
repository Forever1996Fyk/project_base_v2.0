package com.javaweb.MichaelKai.quartz.service;

import com.javaweb.MichaelKai.pojo.ScheduleJob;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 定时任务service
 * @author: YuKai Fan
 * @create: 2019-06-27 14:16
 **/
public interface ScheduleJobService {

    /**
     * 获取所有任务
     * @return
     */
    List<ScheduleJob> getAllScheduleJobs();

    /**
     * 验证scheduleJob是否为null
     * @param scheduleJob
     */
    void checkNotNull(ScheduleJob scheduleJob);

    /**
     * 获取所有正在执行的任务
     * @return
     */
    List<ScheduleJob> getAllRunningJobs() throws SchedulerException;

    /**
     * 新增或者编辑任务
     * @param scheduleJob
     */
    void addOrEditJob(ScheduleJob scheduleJob) throws Exception;

    /**
     * 停止任务
     * @param map
     */
    void pauseJob(Map<String, Object> map) throws SchedulerException;

    /**
     * 删除任务
     * @param map
     */
    void deleteJob(Map<String, Object> map) throws SchedulerException;

    /**
     * 运行任务
     * @param map
     */
    void runOneJob(Map<String, Object> map) throws SchedulerException;

    /**
     * 重启任务
     * @param map
     */
    void resumeJob(Map<String, Object> map) throws SchedulerException;

    /**
     * 根据id获取job
     * @param id
     * @return
     */
    Map<String ,Object> getScheduleJobById(String id);

    /**
     * 根据条件查询任务
     * @param map
     * @return
     */
    List<Map<String, Object>> getScheduleJobs(Map<String, Object> map);

    /**
     * 重启所有任务
     */
    void resumeAllJob() throws SchedulerException;

    /**
     * 暂停所有任务
     */
    void pauseAllJob() throws SchedulerException;
}