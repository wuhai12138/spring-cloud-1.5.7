package com.sfy.quartz.entity.feign.es;

import lombok.Data;

/**
 * @author 金鹏祥
 * @date 2019/5/24 11:44
 * @description
 */
@Data
public class MqErrorLog {
    private String id;
    private String queue;
    private String err_message;
    private Long mtime;
    private String err_data;
}
