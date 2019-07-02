package com.sfy.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sfy.demo.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 系统字典 服务类
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-03-26
 */
public interface ISysDictService extends IService<SysDict> {
    public void testFindAll();
    public IPage<SysDict> findPage(Map<String, Object> map, int page, int pageSize);
}
