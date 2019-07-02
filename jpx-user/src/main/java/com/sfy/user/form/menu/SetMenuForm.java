package com.sfy.user.form.menu;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@ApiModel("菜单设置请求")
@Data
public class SetMenuForm {

    private Integer id;
    private Integer parentId;
    private String name;
    private String url;
    private String path;
    private Integer sort;

}