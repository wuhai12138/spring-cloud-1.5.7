package com.sfy.demo.service;

import com.sfy.demo.entity.SysVoiceDict;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 语音字典 服务类
 * </p>
 *
 * @author 金鹏祥
 * @since 2019-03-26
 */
public interface ISysVoiceDictService extends IService<SysVoiceDict> {
    /****
     * @Description 测试查询
     * @Param []
     * @Author jpx
     * @Version  1.0
     * @Return void
     * @Exception
     * @Date 2019/3/26
     */
    public void findTest();
}
