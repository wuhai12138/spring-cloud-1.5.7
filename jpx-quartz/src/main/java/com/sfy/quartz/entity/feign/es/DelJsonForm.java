package com.sfy.quartz.entity.feign.es;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DelJsonForm {
    @ApiModelProperty("索引（表名）")
    public String indexName;
    @ApiModelProperty("类型（表名）")
    public String esType;
    @ApiModelProperty("jsonArray")
    public JSONArray jsonArray;
}
