package com.sfy.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sfy.user.entity.UserInfo;
import com.sfy.user.dto.user.AppUserDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-06-03
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    /**
     * 查询appUserDto
     * @param userIds
     * @return
     */
    List<AppUserDto> queryList(@Param("userIds") Set<Long> userIds);
}
