package com.javaweb.MichaelKai.controller;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.ScheduleJob;
import com.javaweb.MichaelKai.quartz.service.ScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 定时任务controller
 * @author: YuKai Fan
 * @create: 2019-06-27 11:52
 **/
@RestController
@RequestMapping("/api/quartz")
@Slf4j
public class ScheduleJobController {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 获取所有的任务
     * @return
     */
    @GetMapping("/getAllJobs")
    public Result getAllJobs() {
        log.info("[JobController] the method:getAllJobs! the url path:------------/getAllJobs----------------");
        List<ScheduleJob> jobList = scheduleJobService.getAllScheduleJobs();
        log.info("[JobController] the method:getAllJobs is execution over ");

        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), jobList);
    }

    /**
     * 获取正在执行的任务列表
     * @return
     */
    @GetMapping("/getAllRunningJobs")
    public Result getAllRunningJobs() throws SchedulerException {
        log.info("[JobController] the method:getAllRunningJob! the url path:------------/getAllJobs----------------");
        List<ScheduleJob> runningJobList = scheduleJobService.getAllRunningJobs();
        log.info("[JobController] the method:getAllRunningJob is execution over ");

        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), runningJobList);
    }

    /**
     * 新增或者编辑任务
     * @param scheduleJob
     * @return
     */
    @PostMapping("/addOrEditJob")
    public Result addOrEditJob(@RequestBody  ScheduleJob scheduleJob) {
        log.info("[JobController] the method addOrUpdateJob is start URL path:/addJob, the param:{}", scheduleJob);
        try {
            scheduleJobService.addOrEditJob(scheduleJob);
            return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("[JobController] addOrUpdateJob is failure in method:addOrEditJob！");
            return new Result(true, ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getMessage());
        }
    }

    /**
     * 运行任务
     * @param map 参数
     * @return
     */
    @PostMapping("/runJob")
    public Result runJob(@RequestParam Map<String, Object> map) {
        log.info("[JobController] the url path:------------/runOneJob----------------");
        try {
            scheduleJobService.runOneJob(map);
            return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
        } catch (SchedulerException e) {

            log.error("[JobController] runOnejob is failure in method:runJob");
            return new Result(true, ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getMessage());
        }
    }

    /**
     * 停止任务
     * @param map
     * @return
     */
    @PostMapping("/pauseJob")
    public Result pauseJob(@RequestParam Map<String, Object> map) {
        log.info("[JobController] the url path:------------/pauseJob----------------");
        try {
            scheduleJobService.pauseJob(map);
            return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
        } catch (SchedulerException e) {

            log.error("[JobController] pauseJob is failure in method:pauseJob");
            return new Result(true, ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getMessage());
        }
    }

    /**
     * 删除任务
     * @param map
     * @return
     */
    @DeleteMapping("/deleteJob")
    public Result deleteJob(@RequestParam Map<String, Object> map) {
        log.info("[JobController] the url path:------------/deleteJob----------------");
        try {
            scheduleJobService.deleteJob(map);
            return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
        } catch (SchedulerException e) {
            log.error("[JobController] deleteJob is failure in method:deleteJob");
            return new Result(true, ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getMessage());
        }
    }

    /**
     * 重启任务
     * @param map
     * @return
     */
    @PostMapping("/resumeJob")
    public Result resumeJob(@RequestParam Map<String, Object> map) {
        log.info("[JobController] the url path:------------/resumeJob----------------");
        try {
            scheduleJobService.resumeJob(map);
            return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
        } catch (SchedulerException e) {

            log.error("[JobController] resumeJob is failure in method:resumeJob");
            return new Result(true, ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getMessage());
        }
    }

}