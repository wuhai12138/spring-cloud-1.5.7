package com.sfy.quartz.form;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 金鹏祥
 * @date 2019/6/17 14:23
 * @description
 */
@Data
public class JobForm implements Serializable {
    private String jobClassName;
    private String jobGroupName;
    private Map<String,String> data;
}
