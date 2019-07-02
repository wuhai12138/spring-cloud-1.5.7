package com.sfy.user.form.pad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 金鹏祥
 * @date 2019/5/28 11:54
 * @description
 */
@Data
public class ProductBindResult {
    @ApiModelProperty(value="pad代码")
   String productCode;
    @ApiModelProperty(value="pad名称")
   String productName;
}
