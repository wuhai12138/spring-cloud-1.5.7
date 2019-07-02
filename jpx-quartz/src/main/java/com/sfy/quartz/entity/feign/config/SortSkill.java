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
@ApiModel(value="SortSkill对象", description="")
public class SortSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date date;

    private String reType;

    private String showField;

    private String id;

    private String name;

    private String match;

    private String reKey;

    private String skillSortField;

    private String queryField;

    private String skill;

    private Integer sort;


}
