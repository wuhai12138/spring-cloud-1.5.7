package com.sfy.user.form.user;

import lombok.Data;

import java.util.List;

/**
 * Created by 金鹏祥 on 2019/5/22.
 */
@Data
public class SetUserRolesForm {

    private Long userId;
    private List<Long> roleIds;
}
