package com.sfy.demo.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sfy.demo.entity.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统字典 Mapper 接口
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-03-26
 */
public interface SysDictMapper extends BaseMapper<SysDict>  {
    List<SysDict> findPage(Page<SysDict> page, @Param("params") Map<String, Object> map);
}
