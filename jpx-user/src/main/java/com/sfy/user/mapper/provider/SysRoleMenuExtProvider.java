package com.sfy.user.mapper.provider;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 金鹏祥 on 2019/5/14.
 */
public class SysRoleMenuExtProvider {

    public String getRoleIds(Map<String,Object> conditions) {

        List<Integer> menuIds = (List<Integer>) conditions.get("menuIds");
        if (menuIds == null || menuIds.size() == 0) {
            return null;
        }
        return "select role_id from sys_role_menu where menu_id in (" + StringUtils.join(menuIds, ",") + ")";
    }
}
