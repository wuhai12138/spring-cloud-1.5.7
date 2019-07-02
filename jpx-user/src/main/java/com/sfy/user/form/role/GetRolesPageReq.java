package com.sfy.user.form.role;

import com.sfy.user.form.PageReq;
import lombok.Data;

/**
 * Created by 金鹏祥 on 2019/5/14.
 */
@Data
public class GetRolesPageReq extends PageReq {

    private Long id;
    private String name;
}
