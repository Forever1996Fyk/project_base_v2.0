package com.javaweb.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @program: project_base
 * @description: Timer创建简单的定时任务
 * @author: YuKai Fan
 * @create: 2019-06-26 10:06
 **/
public class TimerDemo {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("TimerTask1 run" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        },1000, 5000);
    }
}