package com.sfy.user.mapper.extend;

import com.sfy.user.mapper.provider.SysRoleMenuExtProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by 金鹏祥 on 2019/3/27.
 */
public interface SysRoleMenuExtMapper {

    @SelectProvider(type = SysRoleMenuExtProvider.class, method = "getRoleIds")
    List<Long> getRoleIds(@Param("menuIds")List<Integer> menuIds);
}