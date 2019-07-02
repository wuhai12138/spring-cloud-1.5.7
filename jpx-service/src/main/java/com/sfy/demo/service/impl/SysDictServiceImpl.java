package com.sfy.demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lorne.tx.annotation.TxTransaction;
import com.sfy.boot.exception.SFYException;
import com.sfy.demo.entity.SysDict;
import com.sfy.demo.mapper.SysDictMapper;
import com.sfy.demo.service.IAuthTestServiceClient;
import com.sfy.demo.service.ISysDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sfy.utils.constant.ConstantSFY;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统字典 服务实现类
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-03-26
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {
    @Resource
    private SysDictMapper sysDictMapper;
    @Resource
    private IAuthTestServiceClient authTestServiceClient;

    @Override
    @Transactional
    @TxTransaction
    public void testFindAll() {
//        sysDictMapper.selectByMap(null);
        List<SysDict> list = new ArrayList();
        SysDict sysDict = new SysDict();
        sysDict.setDictId("2222222");
        sysDict.setDictName("2222222");
        list.add(sysDict);
        authTestServiceClient.save();

//        this.save(sysDict);
        sysDict = new SysDict();
        sysDict.setDictId("3333");
        sysDict.setDictName("3333");
        list.add(sysDict);
        try {
            this.saveBatch(list);
//            int count = 100/0;
        }catch (Exception e){
            throw new SFYException(ConstantSFY.CODE_500, e.getMessage());
        }
    }

    @Override
    public IPage<SysDict> findPage(Map<String, Object> map, int page, int pageSize) {
        Page<SysDict> p = new Page<>(page, pageSize);
        p.setRecords(sysDictMapper.findPage(p, map));
        return p;
    }
}
