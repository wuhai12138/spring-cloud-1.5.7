package com.sfy.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix="myConfig") //接收application.yml中的myProps下面的属性
@Data
public class MyConfig {
    private List<String> notAuthList;
    private String devUrl;
}
