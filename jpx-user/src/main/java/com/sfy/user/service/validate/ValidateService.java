package com.sfy.user.service.validate;

import com.alibaba.fastjson.JSON;
import com.sfy.boot.exception.SFYException;
import com.sfy.user.config.RabbitConstants;
import com.google.common.base.Strings;
import com.sfy.user.constant.UserConstant;
import com.sfy.user.form.validate.SendMobileCodeForm;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.jg.JGMessageInfo;
import com.sfy.user.dto.validate.ImageCode;
import com.sfy.user.utils.ConstantSFY;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by 金鹏祥 on 2019/5/14.
 */
@Slf4j
@Service
public class ValidateService {

    @Autowired
    ImageCodeGenerator imageCodeGenerator;
    @Autowired
    RedisTemplate redisTemplate;
//    @Autowired
//    JSmsService jSmsService;
    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * 图片验证码
     *
     * @param requestId
     * @param response
     * @throws IOException
     */
    public ApiResult image(String requestId, HttpServletResponse response) throws IOException {

        Assert.notNull(requestId, "requestId 不能为空");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        ImageCode imageCode = imageCodeGenerator.generate();
        ValueOperations operations = redisTemplate.opsForValue();
        String redisKey = getImageRedisKey(requestId);
        operations.set(redisKey, imageCode.getCode());
        redisTemplate.expire(redisKey, UserConstant.getRedisTimeOut(1), TimeUnit.SECONDS);
        try (ServletOutputStream out = response.getOutputStream()) {
            ImageIO.write(imageCode.getImage(), "JPEG", out);
        }

        return ApiResult.success(null);
    }

    /**
     * 发送一个短信验证码，验证码有效时间为180秒，
     * 每申请发送一次短信验证码，就需要提供一次不同的imgCode，从而防止接口被刷
     */
    public ApiResult<Boolean> mobile(SendMobileCodeForm request) throws SFYException {

//        if (Strings.isNullOrEmpty(request.getRequestId())){
//            throw new SFYException(ConstantSFY.CODE_500, "requestId不能为空");
//        }
//        if (Strings.isNullOrEmpty(request.getImgCode())){
//            throw new SFYException(ConstantSFY.CODE_500, "imgCode不能为null");
//        }
        if (Strings.isNullOrEmpty(request.getMobile())){
            throw new SFYException(ConstantSFY.CODE_500, "手机号不能为null");
        }
//
//        if (!verifyImgCode(request.getRequestId(), request.getImgCode())){
//            throw new SFYException(ConstantSFY.CODE_500, "图形验证码错误");
//        }
        //发送手机验证码
        amqpTemplate.convertAndSend(RabbitConstants.JG_MESSAGE, JSON.toJSONString(new JGMessageInfo(request.getMobile())));
        return ApiResult.success(true);
    }

    /**
     * 校验图形验证码是否有效
     *
     * @param requestId
     * @param imgCode
     * @return
     */
    public boolean verifyImgCode(String requestId, String imgCode) {

        String redisKey = getImageRedisKey(requestId);
        return verifyCode(redisKey, imgCode);
    }

    /**
     * 验证手机短信验证码
     *
     * @param mobile
     * @param smsCode
     * @return
     */
    public boolean verifyMobileCode(String mobile, String smsCode) {

        String redisKey = getMobileRedisKey(mobile);
        return verifyCode(redisKey, smsCode);
    }

    /**
     * 验证 比对redis Code
     *
     * @param redisKey
     * @param code
     * @return
     */
    private boolean verifyCode(String redisKey, String code) {

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String cacheCode = operations.get(redisKey);
        if (cacheCode == null || !cacheCode.equalsIgnoreCase(code)){
            return false;
        }
        //有效验证一次
        redisTemplate.delete(redisKey);
        return true;
    }

//    /**
//     * 发送手机验证码
//     *
//     * @param mobile
//     * @throws Exception
//     */
//    public void sendMobileCode(String mobile) throws Exception {
//
//        ValueOperations<String, String> operations = redisTemplate.opsForValue();
//        //记录数据到缓存
//
//        Integer smsCode = new Random().nextInt(8999) + 1000;
//        jSmsService.sendSMSCode(mobile, "" + smsCode);
//        //记录发往telNo的短信验证码为smsCode
//        String redisKey = getMobileRedisKey(mobile);
//        operations.set(redisKey, "" + smsCode);
//        redisTemplate.expire(redisKey, UserConstant.getRedisTimeOut(1), TimeUnit.SECONDS);
//    }

    /**
     * 获取手机验证码 redis key
     *
     * @param mobile
     * @return
     */
    private String getMobileRedisKey(String mobile) {
        return UserConstant.Validate_Mobile_Redis_Key + mobile;
    }

    /**
     * 图片验证码 redis key
     *
     * @param requestId
     * @return
     */
    private String getImageRedisKey(String requestId) {
        return UserConstant.Validate_Image_Redis_Key + requestId;
    }

}
