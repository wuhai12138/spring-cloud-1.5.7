package com.sfy.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统字典
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dict")
@ApiModel(value = "SysDict对象", description = "系统字典")
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dictId;

    private String dictType;

    private String dictCode;

    private String dictName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String infoJson;
}
