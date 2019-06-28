package com.javaweb.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: project_base
 * @description: 使用ScheduledThreadPoolExecutor 任务调度线程池创建定时任务
 * @author: YuKai Fan
 * @create: 2019-06-26 10:17
 **/
public class SchedulerDemo {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                System.out.println("ScheduledThreadPoolExecutor1 run:" + now);
            }
        },1,2, TimeUnit.SECONDS);
    }
}