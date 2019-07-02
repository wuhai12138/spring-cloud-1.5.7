package com.sfy.user.dto.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@ApiModel("菜单数据模型")
@Data
public class MenuDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer parentId;
    private String name;
    private String url;
    private String path;
    private Integer sort;
    private Byte hidden;
    @ApiModelProperty("是否已经选中，用于展示")
    private int checked;

    private List<MenuDto> subList;
}
