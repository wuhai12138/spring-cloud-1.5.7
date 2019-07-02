package com.sfy.admin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
public class Configs {
    @Value("${eureka-server}")
    private String eureka;
    @Value("${security-name}")
    private String securityName;
    @Value("${security-password}")
    private String securityPassword;
    @Value("${rabbitmq-host}")
    private String rabbitmqHost;
    @Value("${rabbitmq-port}")
    private String rabbitmqPort;
    @Value("${rabbitmq-username}")
    private String rabbitmqUsername;
    @Value("${rabbitmq-password}")
    private String rabbitmqPassword;
    @Value("${rabbitmq-virtualHost}")
    private String rabbitmqVirtualHost;
    @Value("${lease-renewal-interval-in-seconds}")
    private String intervalInSeconds;
    @Value("${lease-expiration-duration-in-seconds}")
    private String durationInSeconds;
}