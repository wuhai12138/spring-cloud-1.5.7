package com.sfy.quartz.entity.feign.config;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysSkill对象", description="")
public class SysSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String content;

    private String value;

    private Date date;

    private Integer sort;


}
