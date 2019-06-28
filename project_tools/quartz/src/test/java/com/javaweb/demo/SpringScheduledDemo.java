package com.javaweb.demo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-26 10:21
 **/
@Component
public class SpringScheduledDemo {
    @Scheduled(cron = "1/5 * * * * ?")
    public void testScheduled() {
        System.out.println("springScheduled run:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}