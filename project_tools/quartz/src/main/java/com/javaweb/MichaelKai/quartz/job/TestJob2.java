package com.javaweb.MichaelKai.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-28 09:41
 **/
@Slf4j
@Service
public class TestJob2 {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
    public void execute() {
        log.info("[TestJob2]测试这个任务调度框架是否管用！");
        log.info("----------------------------------:{}",sdf.format(new Date()));
    }
}