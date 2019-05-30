package com.javaweb.MichaelKai.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: project_base
 * @description: 文件上传
 * @author: YuKai Fan
 * @create: 2019-05-30 10:16
 **/
@Component
@ConfigurationProperties(prefix = "project.fileupload")
@Data
public class FileUploadProperties {

    //本地模式下的上传路径
    private String FILE_PATH = "E:/upload/";
    /*//开发模式下的上传路径
    private String FILE_PATH = "E:/upload";
    //部署环境下的上传路径
    private String FILE_PATH = "";*/
}