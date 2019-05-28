package com.javaweb.MichaelKai.thymeleaf.config;

import com.javaweb.MichaelKai.thymeleaf.BaseDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-05-24 15:06
 **/
@Configuration
public class ThymeleafConfig {

    /**
     * 配置自定义的CusDialect，用于整合thymeleaf模板
     */
    @Bean
    public BaseDialect getBaseDialect(){
        return new BaseDialect();
    }
}
