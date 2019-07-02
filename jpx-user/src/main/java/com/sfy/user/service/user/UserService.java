package com.sfy.user.service.user;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Strings;
import com.sfy.boot.exception.SFYException;
import com.sfy.user.config.RabbitConstants;
import com.sfy.user.entity.*;
import com.sfy.user.form.FirstChangePwdForm;
import com.sfy.user.form.user.*;
import com.sfy.user.form.validate.SendMobileCodeForm;
import com.sfy.user.mapper.UserInfoMapper;
import com.sfy.user.mapper.UserMapper;
import com.sfy.user.mapper.UserRolesMapper;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.jg.JGUserInfo;
import com.sfy.user.dto.user.UserDtoInfo;
import com.sfy.user.dto.user.UserSimpleInfo;
import com.sfy.user.service.role.RoleService;
import com.sfy.user.service.validate.ValidateService;
import com.sfy.user.utils.ConstantSFY;
import com.sfy.user.utils.DozerMapper;
import com.sfy.user.utils.RandomGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    Md5PasswordEncoder passwordEncoder;
    @Resource
    UserMapper userMapper;
    @Resource
    UserRolesMapper userRolesMapper;
    @Autowired
    RoleService roleService;
    @Autowired
    ValidateService validateService;
    @Resource
    UserInfoMapper tUserInfoMapper;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * 获取当前用户信息
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public UserDtoInfo getCurUserInfo() {
        UserDtoInfo userDtoInfo = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
            authentication = oAuth2Auth.getUserAuthentication();

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
                userDtoInfo = (UserDtoInfo) authenticationToken.getPrincipal();
            } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
                // 刷新token方式
                PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken) authentication;
                userDtoInfo = (UserDtoInfo) authenticationToken.getPrincipal();
            }
        }
        if (userDtoInfo != null)
            userDtoInfo.setPassword("");
        return userDtoInfo;
    }

    /**
     * 当前用户
     *
     * @return
     */
    public ApiResult<UserDtoInfo> current() {
        UserDtoInfo userDtoInfo = getCurUserInfo();
        if (userDtoInfo == null) {
            return ApiResult.error(ConstantSFY.CODE_204, ConstantSFY.MESSAGE_204);
        }
        return ApiResult.success(userDtoInfo);
    }

    /**
     * 用户简单信息
     *
     * @return
     */
    public UserSimpleInfo getUserSimpleInfo() {

        UserDtoInfo userDtoInfo = getCurUserInfo();
        if (userDtoInfo == null) {
            return null;
        }
        UserSimpleInfo simpleInfo = new UserSimpleInfo();
        simpleInfo.setId(userDtoInfo.getId());
        simpleInfo.setShopId(userDtoInfo.getShopId());
        simpleInfo.setShopName(userDtoInfo.getShopName());
        simpleInfo.setUsername(userDtoInfo.getUsername());
        simpleInfo.setFirstName(userDtoInfo.getFirstName());
        simpleInfo.setLastName(userDtoInfo.getLastName());
        simpleInfo.setEmail(userDtoInfo.getEmail());
        simpleInfo.setImageUrl(userDtoInfo.getImageUrl());
        simpleInfo.setEnabled(userDtoInfo.getEnabled());
        simpleInfo.setCreateTime(userDtoInfo.getCreateTime());
        simpleInfo.setUpdateTime(userDtoInfo.getUpdateTime());
        return simpleInfo;
    }

    /**
     * 设置用户是否可用
     *
     * @param userIds
     * @param enabled
     * @return
     */
    public boolean setUsersEnabled(List<Long> userIds, boolean enabled) {

        if (userIds.contains(1)) {
            //超级管理员
            return false;
        }

        User user = new User();
        if (enabled) {
            user.setEnabled(1);

        } else {
            user.setEnabled(0);
        }
        return userMapper.update(user, Wrappers.<User>lambdaUpdate().in(User::getId, userIds)) > 0;
    }

    /**
     * 查询用户列表
     *
     * @param request
     * @return
     */
    public ApiResult<IPage<User>> getPageList(IPage<User> page, GetUsersReq request) {

        try {
            IPage<User> userIPage = userMapper.selectPage(page, Wrappers.<User>lambdaQuery()
                    .eq(request.getId() != null && request.getId() > 0, User::getId, request.getId())
                    .like(!Strings.isNullOrEmpty(request.getUsername()), User::getUsername, request.getUsername())
                    .eq(!Strings.isNullOrEmpty(request.getEmail()), User::getEmail, request.getEmail())
                    .eq(!Strings.isNullOrEmpty(request.getMobile()), User::getMobile, request.getMobile())
                    .gt(request.getBeginTime() != null && request.getBeginTime() > 0, User::getCreateTime, new Date(request.getBeginTime()))
                    .lt(request.getEndTime() != null && request.getEndTime() > 0, User::getCreateTime, new Date(request.getEndTime())).orderByAsc(User::getCreateTime));

            return ApiResult.success(userIPage);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new SFYException(ConstantSFY.CODE_500, ConstantSFY.MESSAGE_500);
        }
    }

    /**
     * 获取用户角色ID列表
     *
     * @param userId
     * @return
     */
    public List<Long> getRoleIds(Long userId) {

        List<Long> ids = new ArrayList<>();
        List<UserRoles> entities = userRolesMapper.selectList(Wrappers.<UserRoles>lambdaQuery().eq(UserRoles::getUserId, userId));
        if (entities == null || entities.size() == 0) {
            return ids;
        }

        entities.parallelStream().forEach(entity -> {
            ids.add(entity.getRolesId());
        });
        return ids;
    }

    /**
     * 根据用户名查找用户信息
     *
     * @param username
     * @return
     */
    public UserDtoInfo findByUsername(String username) {

        if (StringUtils.isEmpty(username)) {
            return null;
        }
        try {
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
            if (user != null) {
                UserDtoInfo userDtoInfo = new UserDtoInfo();
                userDtoInfo.setId(user.getId());
                userDtoInfo.setUsername(username);
                userDtoInfo.setPassword(user.getPassword());
                userDtoInfo.setFirstName(user.getFirstName());
                userDtoInfo.setLastName(user.getLastName());
                userDtoInfo.setEmail(user.getEmail());
                userDtoInfo.setImageUrl(user.getImageUrl());
                userDtoInfo.setEnabled(user.getEnabled().byteValue());
                userDtoInfo.setCreateTime(user.getCreateTime());
                userDtoInfo.setUpdateTime(user.getUpdateTime());

                SysUserInfo sysUserInfo = sysUserService.getByUserId(user.getId());
                if (sysUserInfo == null) {
                    userDtoInfo.setShopId(0);
                } else {
                    userDtoInfo.setShopName(sysUserInfo.getShopName());
                    userDtoInfo.setShopId(sysUserInfo.getShopId());
                }
                userDtoInfo.setRoles(roleService.getRoles(user.getId()));
                userDtoInfo.setPermissions(sysUserService.getUserAuths(user.getId()));
                return userDtoInfo;
            }
        } catch (Exception ex) {
            log.error("UserService.findByUsername error:{}", ex.getMessage());
        }
        return null;
    }

    /**
     * 用户注册
     *
     * @param request
     * @return
     */
    public ApiResult<User> register(RegisterForm request) {

        //验证参数
        if (Strings.isNullOrEmpty(request.getMobile())) {
            return ApiResult.error(ConstantSFY.CODE_400, "手机号不能为空！");
        }
        if (Strings.isNullOrEmpty(request.getPassword())) {
            return ApiResult.error(ConstantSFY.CODE_400, "密码不能为空！");
        }
        if (Strings.isNullOrEmpty(request.getCode())) {
            return ApiResult.error(ConstantSFY.CODE_400, "验证码不能为空！");
        }
        if (!validateService.verifyMobileCode(request.getMobile(), request.getCode())) {
            return ApiResult.error(ConstantSFY.CODE_400, "验证码错误！");
        }
        if (Strings.isNullOrEmpty(request.getUsername())) {
            request.setUsername(request.getMobile());
        }
        //check user name
        Integer count = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getUsername, request.getUsername()).or().eq(User::getMobile, request.getMobile()));
        if (count > 0) {
            return ApiResult.error(ConstantSFY.CODE_400, "该用户名/手机号已经存在！");
        }
        User user = DozerMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encodePassword(request.getPassword(), ConstantSFY.PASSWORD_SALT));
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPasswordRevised(1);
        user.setEnabled(1);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        if (userMapper.insert(user) <= 0) {
            return ApiResult.error(ConstantSFY.CODE_400, "注册用户失败，请重试！");
        }

        try {
            //set user profile
            this.setUserRole(user.getId(), 8L);
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user.getId());
            userInfo.setUserCode(request.getUserCode());
            userInfo.setUserNickname(request.getUserNickname());
            userInfo.setUserIcon(request.getUserIcon());
            userInfo.setUserAddress(request.getUserAddress());
            userInfo.setRegisterType(request.getRegisterType());
            userInfo.setRegisterMobile(request.getRegisterMobile());
            userInfo.setMobileVersion(request.getMobileVersion());
            userInfo.setAppVersion(request.getAppVersion());
            userInfo.setDeviceId(request.getDeviceId());
            tUserInfoMapper.insert(userInfo);

            // 同步创建极光用户
            amqpTemplate.convertAndSend(RabbitConstants.JG_USER_REGISTER, JSON.toJSONString(new JGUserInfo(user.getUsername(), userInfo.getUserNickname(), null, System.currentTimeMillis())));

        } catch (Exception ex) {
            log.error("UserService.register error:{}", ex.getMessage());
        }
        return ApiResult.success(user);
    }

    /**
     * 手机号 + 验证码 注册
     *
     * @param request
     * @return
     */
    public ApiResult<User> registerByMobile(MobileRegForm request) {
        //验证参数
        if (Strings.isNullOrEmpty(request.getMobile())) {
            return ApiResult.error(ConstantSFY.CODE_400, "手机号不能为空！");
        }
        if (Strings.isNullOrEmpty(request.getCode())) {
            return ApiResult.error(ConstantSFY.CODE_400, "验证码不能为空！");
        }
        if (!validateService.verifyMobileCode(request.getMobile(), request.getCode())) {
            return ApiResult.error(ConstantSFY.CODE_400, "验证码错误！");
        }

        //check user name
        Integer count = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getUsername, request.getMobile()).or().eq(User::getMobile, request.getMobile()));
        if (count > 0) {
            return ApiResult.error(ConstantSFY.CODE_400, "该用户名/手机号已经存在！");
        }

        User user = DozerMapper.map(request, User.class);
        user.setUsername(request.getMobile());
        user.setPassword(passwordEncoder.encodePassword("666666", ConstantSFY.PASSWORD_SALT));
        user.setEnabled(1);
        user.setPasswordRevised(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        if (userMapper.insert(user) <= 0) {
            return ApiResult.error(ConstantSFY.CODE_400, "注册用户失败，请重试！");
        }

        try {
            this.setUserRole(user.getId(), 8L);
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user.getId());
            userInfo.setRegisterType(request.getRegisterType());
            tUserInfoMapper.insert(userInfo);

            // 同步创建用户 非主流程
            amqpTemplate.convertAndSend(RabbitConstants.JG_USER_REGISTER,
                    JSON.toJSONString(new JGUserInfo(user.getUsername(), user.getUsername(), null, System.currentTimeMillis())));
        } catch (Exception ex) {
            log.error("UserService.registerByMobile error:{}", ex.getMessage());
        }
        return ApiResult.success(user);
    }


    /**
     * 设置用户默认角色
     *
     * @param userId
     * @return
     */
    public boolean setUserRole(Long userId, Long roleId) {

        //TODO 待测试(重点)
        UserRoles userRoles = new UserRoles();
        userRoles.setRolesId(roleId);
        userRoles.setUserId(userId);
        Integer count = userRolesMapper.selectCount(Wrappers.<UserRoles>lambdaQuery().eq(UserRoles::getRolesId, roleId).eq(UserRoles::getUserId, userId));
        if (count == 0) {
            userRolesMapper.insert(userRoles);
        }
        return true;
    }

    /**
     * 修改密码请求
     *
     * @param request
     * @return
     */
    public ApiResult<User> changePwd(ChangePwdForm request) {

        if (Strings.isNullOrEmpty(request.getUsername())) {
            return ApiResult.error(ConstantSFY.CODE_400, "用户名不能为空！");
        }
        if (Strings.isNullOrEmpty(request.getPassword())) {
            return ApiResult.error(ConstantSFY.CODE_400, "密码不能为空！");
        }
        if (Strings.isNullOrEmpty(request.getNewPassword())) {
            return ApiResult.error(ConstantSFY.CODE_400, "新密码不能为空！");
        }
        User user = getByNameAndPwd(request.getUsername(), request.getPassword());
        if (user == null) {
            return ApiResult.error(ConstantSFY.CODE_400, "未找到用户信息！");
        }
        user.setPassword(passwordEncoder.encodePassword(request.getNewPassword(), ConstantSFY.PASSWORD_SALT));
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordRevised(1);
        if (userMapper.updateById(user) <= 0) {
            return ApiResult.error(ConstantSFY.CODE_400, "密码修改失败，请重试！");
        }
        user.setPassword("");
        return ApiResult.success(user);
    }


    /**
     * 根据用户手机号查找用户账号
     *
     * @param request
     * @return
     */
    public ApiResult findAccount(FindAccountForm request) {

        User user = getByMobile(request.getMobile());
        if (user == null) {
            return ApiResult.error(ConstantSFY.CODE_400, "未找到该手机号用户信息！");
        }
        try {
            validateService.mobile(DozerMapper.map(request, SendMobileCodeForm.class));
        } catch (SFYException ex) {
            return ApiResult.error(ex.getCode(), ex.getMsg());
        } catch (Exception ex) {
            return ApiResult.error(ConstantSFY.CODE_400, ex.getMessage());
        }
        return ApiResult.success("请查收找回账号验证码！");
    }

    /**
     * 重置密码
     *
     * @param request
     * @return
     */
    public ApiResult<User> resetPwd(ResetPwdForm request) {

        if (Strings.isNullOrEmpty(request.getMobile())) {
            return ApiResult.error(ConstantSFY.CODE_400, "手机号不能为空！");
        }
        if (Strings.isNullOrEmpty(request.getCode())) {
            return ApiResult.error(ConstantSFY.CODE_400, "验证码不能为空！");
        }
        if (!validateService.verifyMobileCode(request.getMobile(), request.getCode())) {
            return ApiResult.error(ConstantSFY.CODE_400, "验证码错误！");
        }
        User user = getByMobile(request.getMobile());
        if (user == null) {
            return ApiResult.error(ConstantSFY.CODE_400, "未找到用户信息！");
        }
        String resetPwd = new RandomGenerator(6).generateInt();
        user.setPassword(passwordEncoder.encodePassword(resetPwd, ConstantSFY.PASSWORD_SALT));
//        user.setPassword(passwordEncoder.encode(resetPwd));
        user.setPasswordRevised(1);
        if (userMapper.updateById(user) <= 0) {
            return ApiResult.error(ConstantSFY.CODE_400, "重置密码失败，请重试！");
        }
        user.setPassword("");
        return ApiResult.success(user);
    }

    /**
     * 第一次设置密码
     *
     * @param request
     * @return
     */
    public ApiResult<User> setPwd(FirstChangePwdForm request) {
        if (Strings.isNullOrEmpty(request.getUsername())) {
            return ApiResult.error(ConstantSFY.CODE_400, "手机号不能为空！");
        }
        if (Strings.isNullOrEmpty(request.getNewPassword())) {
            return ApiResult.error(ConstantSFY.CODE_400, "新密码不能为空！");
        }

        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, request.getUsername()));
        if (user == null || user.getPasswordRevised() != 0) {
            return ApiResult.error(ConstantSFY.CODE_500, "您已设置过密码");
        }
        user.setPassword(passwordEncoder.encodePassword(request.getNewPassword(), ConstantSFY.PASSWORD_SALT));
        user.setPasswordRevised(1);
        if (userMapper.updateById(user) <= 0) {
            return ApiResult.error(ConstantSFY.CODE_400, "密码设置失败，请重试！");
        }
        user.setPassword("");
        return ApiResult.success(user);
    }

    /**
     * 获取用户列表
     *
     * @param ids
     * @return
     */
    public List<User> getUsersByIds(List<Long> ids) {
        return userMapper.selectList(Wrappers.<User>lambdaQuery().in(User::getId, ids));
    }

    /**
     * 获取用户Map
     *
     * @param ids
     * @return
     */
    public Map<Long, User> getUserMapByIds(List<Long> ids) {

        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().in(User::getId, ids));
        if (users == null || users.size() == 0) return null;
        Map<Long, User> userMap = new HashMap<>();
        users.parallelStream().forEach(user -> {
            userMap.put(user.getId(), user);
        });
        return userMap;
    }

    /**
     * 根据用户名获取用户
     *
     * @param username
     * @return
     */
    public User getByUsername(String username) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username).eq(User::getEnabled, 1));
    }

    /**
     * 根据手机号获取用户
     *
     * @param mobile
     * @return
     */
    public User getByMobile(String mobile) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getMobile, mobile).eq(User::getEnabled, 1));
    }

    /**
     * 根据用户名密码获取用户信息
     *
     * @param username
     * @param password
     * @return
     */
    public User getByNameAndPwd(String username, String password) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username).eq(User::getPassword, passwordEncoder.encodePassword(password, ConstantSFY.PASSWORD_SALT)));
    }

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    public User get(Long id) {
        return userMapper.selectById(id);
    }

    public ApiResult<List<UserDtoInfo>> findUserByNames(Set<String> userNames) {
        if (CollectionUtils.isEmpty(userNames)) {
            return ApiResult.error(ConstantSFY.CODE_4000, ConstantSFY.MESSAGE_4000);
        }
        try {
            List<User> userList = userMapper.selectList(Wrappers.<User>lambdaQuery().in(User::getUsername, userNames));
            if (!CollectionUtils.isEmpty(userList)) {
                List<UserDtoInfo> dtoSet = new ArrayList<>(userList.size());
                for (User user : userList) {
                    UserDtoInfo userDtoInfo = new UserDtoInfo();
                    BeanUtils.copyProperties(user, userDtoInfo);
                    dtoSet.add(userDtoInfo);
                }
                return ApiResult.success(dtoSet);
            }
        } catch (Exception ex) {
            log.error("UserService.findUserByNames error:{}", ex.getMessage());
        }
        return ApiResult.error(ConstantSFY.CODE_500, ConstantSFY.MESSAGE_500);
    }

    /**
     * 设置用户是否启用
     * @param userId
     * @param enabled
     * @return
     */
    public boolean setEnabled(Long userId, Integer enabled) {
        User user = get(userId);
        if (user == null)
            return false;
        user.setEnabled(enabled);
        return userMapper.updateById(user) > 0;
    }
}