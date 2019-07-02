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
@ApiModel(value="DialogueSkill对象", description="")
public class DialogueSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private Date date;

    private String issue;

    private String answer;

    private String url;

    private Integer sort;


}
