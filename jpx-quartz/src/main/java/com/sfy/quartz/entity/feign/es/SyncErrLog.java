package com.sfy.quartz.entity.feign.es;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 金鹏祥
 * @date 2019/5/22 9:24
 * @description
 */
@Data
public class SyncErrLog {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "操作类型")
    private String event_type;
    @ApiModelProperty(value = "错误数据集名称")
    private String err_index;
    @ApiModelProperty(value = "错误数据集类型")
    private String err_es_type;
    @ApiModelProperty(value = "错误数据id")
    private String err_id;
    @ApiModelProperty(value = "错误数据")
    private String err_data;
    @ApiModelProperty(value = "错误数据时间")
    private Long err_date;
}
