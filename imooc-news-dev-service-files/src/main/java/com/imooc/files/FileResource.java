package com.imooc.files;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zimin
 * @function 文件配置类
 */
@ComponentScan
@PropertySource("classpath:file-dev.properties")
@EnableConfigurationProperties
@ConfigurationProperties(prefix= "file")
public class FileResource {
    private String  host;
}
