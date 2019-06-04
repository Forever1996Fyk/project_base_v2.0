package com.javaweb.MichaelKai.entity;

import lombok.Data;

/**
 * @program: project_base
 * @description: 监视信息entity
 * @author: YuKai Fan
 * @create: 2019-06-04 09:25
 **/
@Data
public class MonitorInfoEntity {

    //可使用内存
    private long totalMemory;

    //剩余内存
    private long freeMemory;

    //最大可使用内存
    private long maxMemory;

    //当前操作系统
    private String osName;

    //剩余的物理内存
    private long freePhysicalMemorySize;

    //已使用的物理内存
    private long usedMemory;

    //线程总数
    private int totalThread;

    //cpu使用率
    private double cpuRatio;

    //总物理内存
    private long totalMemorySize;

    //运行内存占用率
    private double ramRatio;
}