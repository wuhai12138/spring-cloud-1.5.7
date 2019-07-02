package com.sfy.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/3/26.
 */
@Data
@ApiModel("分页参数")
public class PageReq {
    @ApiModelProperty("当前页")
    private int currentPage = 0;
    @ApiModelProperty("页码")
    private int pageSize = 10;
}
