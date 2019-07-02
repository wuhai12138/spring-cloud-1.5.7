package com.sfy.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sfy.user.entity.Authority;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-06-03
 */
public interface AuthorityMapper extends BaseMapper<Authority> {
    List<Authority> findAuthorityByUserId(@Param("params") Map<String, Object> map);
}
