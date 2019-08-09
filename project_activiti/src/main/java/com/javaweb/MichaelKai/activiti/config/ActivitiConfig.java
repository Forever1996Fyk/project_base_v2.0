package com.javaweb.MichaelKai.activiti.config;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @ClassName ActivitiConfig
 * @Description TODO
 * @Author YuKai Fan
 * @Date 2019/8/9 23:01
 * @Version 1.0
 **/
@Configuration
public class ActivitiConfig {

    private String dataType = "mysql";

    /**
     * @Description 初始化流程引擎
     *
     * @Author YuKai Fan
     * @Date 23:11 2019/8/9
     * @Param
     * @return
     **/
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(DataSource dataSource, PlatformTransactionManager transactionManager) {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource);
        //表不存在创建表
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        //指定数据库
        processEngineConfiguration.setDatabaseType(dataType);
        processEngineConfiguration.setTransactionManager(transactionManager);
        //历史变量
        processEngineConfiguration.setHistory("full");
        //指定字体
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");

        processEngineConfiguration.setProcessDiagramGenerator(new DefaultProcessDiagramGenerator());
        return processEngineConfiguration;
    }

    /**
     * @Description 流程引擎配置整合到factoryBean
     *
     * @Author YuKai Fan
     * @Date 23:13 2019/8/9
     * @Param
     * @return
     **/
    @Bean
    public ProcessEngineFactoryBean processEngine(ProcessEngineConfiguration processEngineConfiguration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        return processEngineFactoryBean;
    }
}
