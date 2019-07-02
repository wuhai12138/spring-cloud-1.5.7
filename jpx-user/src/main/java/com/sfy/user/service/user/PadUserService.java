package com.sfy.user.service.user;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sfy.boot.exception.SFYException;
import com.sfy.user.config.RabbitConstants;
import com.sfy.user.entity.ProductInfo;
import com.sfy.user.entity.User;
import com.sfy.user.form.pad.ProductBindResult;
import com.sfy.user.form.user.PadRegForm;
import com.sfy.user.mapper.ProductInfoMapper;
import com.sfy.user.mapper.UserMapper;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.jg.JGUserInfo;
import com.sfy.user.dto.user.PadUserDto;
import com.sfy.user.dto.user.UserDto;
import com.sfy.user.dto.user.UserDtoInfo;
import com.sfy.user.client.IProductServiceClient;
import com.sfy.user.utils.ConstantSFY;
import com.sfy.user.utils.DozerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 金鹏祥
 * @date 2019/5/27 17:20
 * @description
 */
@Slf4j
@Service
public class PadUserService {
    @Resource
    private ProductInfoMapper productInfoMapper;
    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;
    @Autowired
    Md5PasswordEncoder passwordEncoder;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private IProductServiceClient productServiceClient;

    /**
     * pad注册
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<User> register(PadRegForm request) {
        Integer count = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getUsername, request.getProductCode()));
        if (count > 0){
            return ApiResult.error(ConstantSFY.CODE_400, "该pad已注册！");
        }

        User user = DozerMapper.map(request, User.class);
        user.setUsername(request.getProductCode());
        user.setPassword(passwordEncoder.encodePassword(request.getProductMac(), ConstantSFY.PASSWORD_SALT));
//        user.setPassword(passwordEncoder.encode(request.getProductMac()));
        user.setEnabled(1);
        user.setPasswordRevised(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        if (userMapper.insert(user) <= 0){
            return ApiResult.error(ConstantSFY.CODE_400, "注册pad用户失败，请重试！");
        }

        try {
            //pad 本身的角色 资料
            userService.setUserRole(user.getId(),8L);
            ProductInfo productInfo = new ProductInfo();
            productInfo.setCreateTime(new Date());
            productInfo.setUpdateTime(new Date());
            productInfo.setUserId(user.getId());
            productInfo.setProductName(request.getProductName());
            productInfo.setProductCode(user.getUsername());
            productInfo.setProductCpuId(request.getProductCpuId());
            productInfo.setPadIcon("写死的默认头像");
            productInfoMapper.insert(productInfo);
            // 默认房间 默认情景模式
            ApiResult apiResult = productServiceClient.initProduct(productInfo.getProductCode());
            if (!"200".equals(apiResult.getCode())){
                throw new SFYException(ConstantSFY.CODE_500,ConstantSFY.MESSAGE_500);
            }
            // todo 默认角色

            // 同步创建用户 非主流程
            amqpTemplate.convertAndSend(RabbitConstants.JG_USER_REGISTER,
                    JSON.toJSONString(new JGUserInfo(user.getUsername(), user.getUsername(), productInfo.getPadIcon(), System.currentTimeMillis())));
        } catch (Exception ex) {
            log.error("padService.register error:{}", ex.getMessage());
            throw new SFYException(ConstantSFY.CODE_500,ConstantSFY.MESSAGE_500);
        }
        return ApiResult.success(user);
    }

    /**
     * 根据用户ID获取pad用户
     * @param userId
     * @return
     */
    public ProductInfo getByUserId(Long userId) {
        return productInfoMapper.selectOne(Wrappers.<ProductInfo>lambdaQuery().eq(ProductInfo::getUserId,userId));
    }

    /**
     * 根据padCode获取pad用户
     * @param productCode
     * @return
     */
    @Transactional
    public ApiResult<PadUserDto> getByProductCode(String productCode) {
        PadUserDto padByProductCode = productInfoMapper.findPadByProductCode(productCode);
        if (padByProductCode == null){
            return ApiResult.error(ConstantSFY.CODE_400,"没有该pad用户",null);
        }
        return ApiResult.success(padByProductCode);
    }

    /**
     * 获取绑定列表
     * @param productCodes
     * @return
     */
    public List<ProductBindResult> listBinds(Set<String> productCodes){
        if (CollectionUtils.isEmpty(productCodes)){
            return null;
        }
        return productInfoMapper.listBinds(productCodes);
    }

    public ApiResult<Set<PadUserDto>> findPadUserByCodes(Set<String> userNames) {
        List<UserDtoInfo> userSet = userService.findUserByNames(userNames).getData();
        if (CollectionUtils.isEmpty(userSet)){
            return ApiResult.error(ConstantSFY.CODE_4000,ConstantSFY.MESSAGE_4000);
        }
        try {
            Set<Long> userIds = userSet.stream().map(UserDto::getId).collect(Collectors.toSet());
            List<PadUserDto> userList = productInfoMapper.queryList(userIds);
            if (!CollectionUtils.isEmpty(userList)) {
                Set<PadUserDto> dtoSet = new HashSet<>(userList.size());
                dtoSet.addAll(userList);
                return ApiResult.success(dtoSet);
            }
        } catch (Exception ex) {
            log.error("UserService.findByUsername error:{}", ex.getMessage());
        }
        return ApiResult.error(ConstantSFY.CODE_500,ConstantSFY.MESSAGE_500);
    }

    public ApiResult<Set<String>> listCodes() {
        Set<String> codes = productInfoMapper.listProductCodes(Wrappers.<ProductInfo>lambdaQuery());
        return ApiResult.success(codes);
    }
}
