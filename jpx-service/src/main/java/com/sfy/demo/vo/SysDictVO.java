package com.sfy.demo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class SysDictVO {
    private String dictId;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "字典编码")
    private String dictCode;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String infoJson;

    private String createTimeStr;
    private String updateTimeStr;

    public void setCreateTime(LocalDateTime createTime){
        this.setCreateTimeStr(createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public void setUpdateTime(LocalDateTime updateTime){
        this.setUpdateTimeStr(updateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
