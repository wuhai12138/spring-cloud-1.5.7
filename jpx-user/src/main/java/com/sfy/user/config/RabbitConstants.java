package com.sfy.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
public class RabbitConstants {
    public static final String EXCHANGE   = "sfyExchange";
    public static final String ROUTINGKEY = "routingsfy";
    public static final String QUEUE      = "queue";
    public static final String JG_MESSAGE = "jg_message";
    public static final String JG_USER_INFO = "jg_user_info";
    public static final String JG_USER_REGISTER = "jg_user_register";
}
