package com.javaweb.michaelKai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-05 09:59
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.javaweb.MichaelKai.mapper")
public class WebSocketApp {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketApp.class, args);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}