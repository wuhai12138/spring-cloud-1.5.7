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
@ApiModel(value="VoiceChildSkill对象", description="")
public class VoiceChildSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String word;

    private String url;

    private Date date;

    private Integer sort;


}
