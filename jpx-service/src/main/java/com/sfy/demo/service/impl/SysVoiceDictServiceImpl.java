package com.sfy.demo.service.impl;

import com.sfy.demo.entity.SysVoiceDict;
import com.sfy.demo.mapper.SysVoiceDictMapper;
import com.sfy.demo.service.ISysVoiceDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 语音字典 服务实现类
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-03-26
 */
@Service
public class SysVoiceDictServiceImpl extends ServiceImpl<SysVoiceDictMapper, SysVoiceDict> implements ISysVoiceDictService {

    @Resource
    private SysVoiceDictMapper sysVoiceDictMapper;

    @Override
    public void findTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("voice_id", "11408");
        sysVoiceDictMapper.selectByMap(null);
    }
}
