package com.sfy.user.form.menu;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@ApiModel("批量菜单隐藏操作")
@Data
public class SetMenusHideForm {

    private List<Integer> menuIds;
    private Byte hidden;

}
