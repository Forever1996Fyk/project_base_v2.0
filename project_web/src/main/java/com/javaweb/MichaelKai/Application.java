package com.javaweb.MichaelKai;

import com.javaweb.MichaelKai.common.utils.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @program: project_base
 * @description: 启动类
 * @author: YuKai Fan
 * @create: 2019-05-17 14:58
 **/
@SpringBootApplication
//扫描dao接口的包，用于识别mybatis
@MapperScan(basePackages = "com.javaweb.MichaelKai.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}