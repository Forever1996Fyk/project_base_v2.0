package com.javaweb.MichaelKai.conf;

import com.javaweb.MichaelKai.common.utils.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @program: project_base
 * @description: bean配置
 * @author: YuKai Fan
 * @create: 2019-05-17 15:45
 **/
@Component
public class BeanConf {

    /**
     * id生成器
     * @return
     */
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }
}