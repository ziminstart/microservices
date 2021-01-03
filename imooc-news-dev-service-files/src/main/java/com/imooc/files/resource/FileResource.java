package com.imooc.files.resource;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zimin
 * @function 文件配置类
 */
@Component
@PropertySource("classpath:file-dev.properties")
@ConfigurationProperties(prefix= "file")
@Data
public class FileResource {
    private String  host;
    private String endpoint;
    private String bucketName;
    private String objectName;
    private String ossHost;
}
