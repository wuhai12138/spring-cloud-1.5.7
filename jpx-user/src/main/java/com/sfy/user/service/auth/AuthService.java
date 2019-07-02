package com.sfy.user.service.auth;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Strings;
import com.sfy.boot.exception.SFYException;
import com.sfy.boot.redis.RedisAuthenticationCodeServices;
import com.sfy.user.entity.*;
import com.sfy.user.form.pad.ProductBindResult;
import com.sfy.user.form.user.*;
import com.sfy.user.mapper.UserInfoMapper;
import com.sfy.user.mapper.UserMapper;
import com.sfy.utils.tools.apiResult.ApiResult;
import com.sfy.user.dto.TokenResult;
import com.sfy.user.dto.user.*;
import com.sfy.user.dto.user.UserDtoInfo;
import com.sfy.user.client.IProductServiceClient;
import com.sfy.user.service.user.PadUserService;
import com.sfy.user.service.user.SysUserService;
import com.sfy.user.service.user.UserService;
import com.sfy.user.service.validate.ValidateService;
import com.sfy.user.utils.ConstantSFY;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Slf4j
@Service
public class AuthService {

    @Autowired
    private Md5PasswordEncoder passwordEncoder;
    @Autowired
    private RedisClientDetailsService redisClientDetailsService;
    @Autowired
    private DefaultTokenServices defaultTokenServices;
    @Autowired
    private AuthorizationServerEndpointsConfiguration authorizationServerEndpointsConfiguration;
    @Autowired
    private RedisTokenStore tokenStore;
    @Autowired
    private ValidateService validateService;
    @Autowired
    private UserService userService;
    @Autowired
    private JPAUserDetailsService jpaUserDetailsService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PadUserService padUserService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisAuthenticationCodeServices redisAuthenticationCodeServices;
    @Autowired
    private HttpServletRequest request;


    /**
     * 系统用户登录
     *
     * @param form
     * @return
     */
    public ApiResult<SysUserLoginInfo> sysUserLogin(SysUserLoginForm form) {

        if (Strings.isNullOrEmpty(form.getUsername())) {
            return ApiResult.error(ConstantSFY.CODE_400, "请输入用户名！");
        }
        if (Strings.isNullOrEmpty(form.getPassword())) {
            return ApiResult.error(ConstantSFY.CODE_400, "请输入用户密码！");
        }
        if (!Strings.isNullOrEmpty(form.getRequestId())) {
            if (Strings.isNullOrEmpty(form.getImageCode())) {
                return ApiResult.error(ConstantSFY.CODE_400, "请输入验证码！");
            } else if (!validateService.verifyImgCode(form.getRequestId(), form.getImageCode())) {
                return ApiResult.error(ConstantSFY.CODE_400, "验证码错误！");
            }
        }


        User user = userService.getByUsername(form.getUsername());
        if (user == null) {
            return ApiResult.error(ConstantSFY.CODE_400, "用户名不存在！");
        }
        if (!passwordEncoder.isPasswordValid(user.getPassword(), form.getPassword(), ConstantSFY.PASSWORD_SALT)) {
//        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
                return ApiResult.error(ConstantSFY.CODE_400, "用户密码错误！");
        }

        SysUserInfo sysUserInfo = sysUserService.getByUserId(user.getId());
        if (sysUserInfo == null) {
            return ApiResult.error(ConstantSFY.CODE_400, "该用户不是系统用户！");
        }
        try {
            sysUserInfo.setIsLogin("1");
            sysUserInfo.setLoginTime(new Date());
            sysUserService.updateById(sysUserInfo);

            OAuth2AccessToken oAuth2AccessToken = createToken(form.getUsername(), form.getPassword());
            SysUserLoginInfo rtn = new SysUserLoginInfo();
            rtn.setUserId(user.getId());
            rtn.setPasswordRevised(user.getPasswordRevised().byteValue());
            rtn.setAccessToken(oAuth2AccessToken.getValue());
            rtn.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
            rtn.setSysUsername(sysUserInfo.getSysUsername());
            rtn.setAccountNumber(sysUserInfo.getAccountNumber());
            rtn.setMenus(sysUserService.getUserMenus(user.getId()));
            return ApiResult.success(rtn);
        } catch (Exception ex) {
            log.error("用户登录失败！");
        }
        return ApiResult.error(ConstantSFY.CODE_400, "登录失败，请重试！");
    }

    /**
     * pad用户登录
     *
     * @param form
     * @return
     */
    public ApiResult<PadUserLoginInfo> padUserLogin(PadUserLoginForm form) {

        User user = userService.getByUsername(form.getProductCode());
        if (user == null) {
            return ApiResult.error(ConstantSFY.CODE_400, "用户名不存在！");
        }
        if (!passwordEncoder.isPasswordValid(user.getPassword(), form.getProductMac(), ConstantSFY.PASSWORD_SALT)) {
            return ApiResult.error(ConstantSFY.CODE_400, "用户密码错误！");
        }

        ProductInfo productInfo = padUserService.getByUserId(user.getId());
        if (productInfo == null) {
            return ApiResult.error(ConstantSFY.CODE_400, "该用户不是pad用户！");
        }
        try {
            OAuth2AccessToken oAuth2AccessToken = createToken(user.getUsername(), user.getPassword());
            PadUserLoginInfo rtn = new PadUserLoginInfo();
            rtn.setUserId(user.getId());
            rtn.setPasswordRevised(user.getPasswordRevised().byteValue());
            rtn.setAccessToken(oAuth2AccessToken.getValue());
            rtn.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
            rtn.setProductCode(productInfo.getProductCode());
            return ApiResult.success(rtn);
        } catch (Exception ex) {
            log.error("pad登录失败！");
        }
        return ApiResult.error(ConstantSFY.CODE_400, "登录失败，请重试！");
    }

    @Resource
    IProductServiceClient productServiceClient;

    /**
     * 用户登录
     *
     * @param form
     * @return
     */
    public ApiResult<AppUserLoginInfo> login(LoginForm form) {

        if (Strings.isNullOrEmpty(form.getUserName())) {
            return ApiResult.error(ConstantSFY.CODE_400, "请输入用户名！");
        }
        if (Strings.isNullOrEmpty(form.getPassword())) {
            return ApiResult.error(ConstantSFY.CODE_400, "请输入用户密码！");
        }
        User user = userService.getByUsername(form.getUserName());
        if (user == null) {
            return ApiResult.error(ConstantSFY.CODE_400, "用户名不存在！");
        }
        if (!passwordEncoder.isPasswordValid(user.getPassword(),form.getPassword(),ConstantSFY.PASSWORD_SALT)){
//        if (!passwordEncoder.matches(form.getPassword(), user.getPassword() )) {
            return ApiResult.error(ConstantSFY.CODE_400, "用户密码错误！");
        }

        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery()
                .eq(UserInfo::getUserId, user.getId()));

        if (userInfo == null) {
            return ApiResult.error(ConstantSFY.CODE_400, "该用户不是App用户！");
        }
        try {

            OAuth2AccessToken oAuth2AccessToken = createToken(form.getUserName(), form.getPassword());

            try {
                //非主流程
                userInfo.setIsLogin("1");
                userInfo.setLoginTime(new Date());
                userInfo.setUpdateTime(new Date());
                userInfoMapper.updateById(userInfo);
            } catch (Exception ex) {
                log.error("ApiResult<AppUserLoginInfo> login {}", ex.getMessage());
            }

            AppUserLoginInfo rtn = new AppUserLoginInfo();
            rtn.setUserId(user.getId());
            rtn.setPasswordRevised(user.getPasswordRevised().byteValue());
            rtn.setAccessToken(oAuth2AccessToken.getValue());
            rtn.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
            rtn.setUsername(user.getUsername());
            rtn.setUserNickname(userInfo.getUserNickname());
            rtn.setUserIcon(userInfo.getUserIcon());
            rtn.setUserAddress(userInfo.getUserAddress());
            rtn.setRegisterType(userInfo.getRegisterType());
            rtn.setRegisterMobile(userInfo.getRegisterMobile());
            rtn.setDeviceId(userInfo.getDeviceId());
            rtn.setCreateTime(userInfo.getCreateTime());
            rtn.setUpdateTime(userInfo.getUpdateTime());
            try {
                Set<String> productCodes = productServiceClient.listBindProductCodes(user.getUsername()).getData();
                if (CollectionUtils.isNotEmpty(productCodes)) {
                    List<ProductBindResult> productBindResults = padUserService.listBinds(productCodes);
                    rtn.setDeviceList(productBindResults);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
            return ApiResult.success(rtn);
        } catch (Exception ex) {
            log.error("用户登录失败！");
        }
        return ApiResult.error(ConstantSFY.CODE_400, "登录失败，请重试！");
    }

    @Resource
    private UserMapper userMapper;

    /**
     * 验证码 登录
     *
     * @param loginForm
     * @return
     */
    @Transactional
    public ApiResult<AppUserLoginInfo> verCodeLogin(VerCodeLoginForm loginForm) throws Exception {

        //check code
        if (!validateService.verifyMobileCode(loginForm.getPhone(), loginForm.getVerCode())) {
            return ApiResult.error(ConstantSFY.CODE_400, "验证码错误！");
        }
        //check user name;
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, loginForm.getPhone())
                .eq(User::getMobile, loginForm.getPhone()));
        UserInfo userInfo = new UserInfo();

        if (user == null) {
            user = new User();
            user.setUsername(loginForm.getPhone());
            user.setMobile(loginForm.getPhone());
            user.setPassword(passwordEncoder.encodePassword("666666", ConstantSFY.PASSWORD_SALT));
//            user.setPassword(passwordEncoder.encode("666666"));
            user.setPasswordRevised(0);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            if (userMapper.insert(user) <= 0) {
                throw new Exception("用户注册失败！");
            }
            userInfo.setUserId(user.getId());
            userInfo.setCreateTime(new Date());
            userInfo.setUpdateTime(new Date());
            userInfo.setLoginTime(new Date());
            userInfo.setIsLogin("1");
            userInfo.setDeviceId(loginForm.getDeviceId());
            userInfo.setRegisterType(loginForm.getRegisterType());
            userInfo.setRegisterMobile(loginForm.getRegisterMobile());
            userInfo.setMobileVersion(loginForm.getMobileVersion());
            userInfo.setIsDel("0");
            userInfo.setRegisterTime(new Date());
            if (userInfoMapper.insert(userInfo) <= 0) {
                throw new Exception("用户注册失败！");
            }

            userService.setUserRole(user.getId(),8L);
        } else {
            userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId,user.getId()));
        }
        String password = "SFY" + loginForm.getVerCode();
        OAuth2AccessToken oAuth2AccessToken = createToken(loginForm.getPhone(), password);

        AppUserLoginInfo rtn = new AppUserLoginInfo();
        rtn.setUserId(user.getId());
        rtn.setPasswordRevised(user.getPasswordRevised().byteValue());
        rtn.setAccessToken(oAuth2AccessToken.getValue());
        rtn.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
        rtn.setUsername(user.getUsername());
        rtn.setUserNickname(userInfo.getUserNickname());
        rtn.setUserIcon(userInfo.getUserIcon());
        rtn.setUserAddress(userInfo.getUserAddress());
        rtn.setRegisterType(userInfo.getRegisterType());
        rtn.setRegisterMobile(userInfo.getRegisterMobile());
        rtn.setDeviceId(userInfo.getDeviceId());
        rtn.setCreateTime(userInfo.getCreateTime());
        rtn.setUpdateTime(userInfo.getUpdateTime());
        try {
            Set<String> productCodes = productServiceClient.listBindProductCodes(user.getUsername()).getData();
            if (CollectionUtils.isNotEmpty(productCodes)) {
                List<ProductBindResult> productBindResults = padUserService.listBinds(productCodes);
                rtn.setDeviceList(productBindResults);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return ApiResult.success(rtn);
    }


    public boolean storeAccessToken(String userName) {

        //用户是否在oauth系统之中
//       UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(userName);
        UserDtoInfo userDtoInfo = userService.findByUsername(userName);
        if (null == userDtoInfo) {
            throw new SFYException(ConstantSFY.CODE_500, ConstantSFY.MESSAGE_500);
        }

        String clientId = "clientId";
        String clientSecret = "secretId";
        String grantType = "password";
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("grant_type", grantType);
        requestParameters.put("client_id", clientId);
        requestParameters.put("client_secret", clientSecret);
        requestParameters.put("username", userName);
        requestParameters.put("password", userDtoInfo.getPassword());

        Set<String> scopes = new HashSet<>();
        scopes.add("all");

        ClientDetails clientDetails = redisClientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
        } else if (!passwordEncoder.isPasswordValid(clientDetails.getClientSecret(), clientSecret, ConstantSFY.PASSWORD_SALT)) {
//        } else if (!passwordEncoder.matches(clientSecret,clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
        }

        Authentication userAuth = new UsernamePasswordAuthenticationToken(userDtoInfo, userDtoInfo.getPassword(), userDtoInfo.getAuthorities());
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientId, scopes, grantType);
        OAuth2RequestFactory oAuth2RequestFactory =
                authorizationServerEndpointsConfiguration.getEndpointsConfigurer().getOAuth2RequestFactory();
        OAuth2Request storedOAuth2Request = oAuth2RequestFactory.createOAuth2Request(clientDetails, tokenRequest);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(storedOAuth2Request, userAuth);
        OAuth2AccessToken existingAccessToken = this.tokenStore.getAccessToken(oAuth2Authentication);

        if (!existingAccessToken.isExpired()) {
            this.tokenStore.storeAccessToken(existingAccessToken, oAuth2Authentication);
            this.storeAuthority(existingAccessToken.getValue(), userDtoInfo.getPermissions());
            return true;
        }

        return false;
    }

    /**
     * 创建Token
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public OAuth2AccessToken createToken(String username, String password) throws Exception {

        String clientId = "clientId";
        String clientSecret = "secretId";
        String grantType = "password";
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("grant_type", grantType);
        requestParameters.put("client_id", clientId);
        requestParameters.put("client_secret", clientSecret);
        requestParameters.put("password", password);
        requestParameters.put("username", username);

        Set<String> scopes = new HashSet<>();
        scopes.add("all");

        ClientDetails clientDetails = redisClientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
        } else if (!passwordEncoder.isPasswordValid(clientDetails.getClientSecret(), clientSecret, ConstantSFY.PASSWORD_SALT)) {
//        } else if (!passwordEncoder.matches(clientSecret,clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
        }
        OAuth2RequestFactory oAuth2RequestFactory =
                authorizationServerEndpointsConfiguration.getEndpointsConfigurer().getOAuth2RequestFactory();
        UserDtoInfo userDtoInfo = userService.findByUsername(username);
        userDtoInfo.setPassword(passwordEncoder.encodePassword(password, ConstantSFY.PASSWORD_SALT));
        Authentication userAuth = new UsernamePasswordAuthenticationToken(userDtoInfo, password, userDtoInfo.getAuthorities());
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientId, scopes, grantType);
        OAuth2Request storedOAuth2Request = oAuth2RequestFactory.createOAuth2Request(clientDetails, tokenRequest);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(storedOAuth2Request, userAuth);
        OAuth2AccessToken oAuth2AccessToken = defaultTokenServices.createAccessToken(oAuth2Authentication);
        redisAuthenticationCodeServices.storeMy(oAuth2AccessToken.getValue(), new PreAuthenticatedAuthenticationToken(oAuth2AccessToken.getValue(), ""));
        this.storeAuthority(oAuth2AccessToken.getValue(), userDtoInfo.getPermissions());


        return oAuth2AccessToken;
    }

    private void storeAuthority(String accessToken, List<Authority> list) {
        List<String> strList = new ArrayList<>();
        if (null != list) {
            if (0 != list.size()) {
                for (Authority en : list) {
                    strList.add(en.getValue());
                }
            }
        }
        redisAuthenticationCodeServices.storeAuthorityList(accessToken, strList);
    }


    /**
     * 获取Token
     *
     * @return
     */
    public ApiResult<TokenResult> getToken() {
        String accessTokenStr = request.getHeader("access-token");
        OAuth2AccessToken oAuth2AccessToken = defaultTokenServices.readAccessToken(accessTokenStr);
        if (oAuth2AccessToken == null) {
            return ApiResult.error(ConstantSFY.CODE_401, "该令牌不存在！");
        }
        return ApiResult.success(getTokenResult(oAuth2AccessToken));
    }

    /**
     * 刷新Token
     *
     * @return
     */
    public ApiResult<TokenResult> refreshToken(String refreshToken, String accessToken) {
        if (StringUtils.isEmpty(refreshToken) || StringUtils.isEmpty(accessToken)) {
            return ApiResult.error(ConstantSFY.CODE_4023, "token不能为空！");
        }

        String clientId = "clientId";
        String clientSecret = "secretId";
        try {
            Set<String> scopes = new HashSet<>();
            scopes.add("all");

            ClientDetails clientDetails = redisClientDetailsService.loadClientByClientId(clientId);
            if (clientDetails == null) {
                throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
            } else if (!passwordEncoder.isPasswordValid(clientDetails.getClientSecret(), clientSecret, ConstantSFY.PASSWORD_SALT)) {
                throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
            }

            Map<String, String> map = new HashMap<>();
            //grant_type、refresh_token、client_id、client_secret
            /*map.put("client_id", clientId);
            map.put("client_secret", clientSecret);*/
            map.put("grant_type", "refresh_token");
            map.put("refresh_token", refreshToken);
            TokenRequest tokenRequest = new TokenRequest(map, clientId, scopes, "refresh_token");
            OAuth2AccessToken oAuth2AccessToken = defaultTokenServices.refreshAccessToken(refreshToken, tokenRequest);
            tokenStore.removeAccessToken(accessToken);
            redisAuthenticationCodeServices.storeMy(oAuth2AccessToken.getValue(), new PreAuthenticatedAuthenticationToken(oAuth2AccessToken.getValue(), ""));
            return ApiResult.success(getTokenResult(oAuth2AccessToken));
        } catch (Exception ex) {
            log.error("refreshToken error:{}", ex.getMessage());
        }
        return ApiResult.error(ConstantSFY.CODE_401, ConstantSFY.MESSAGE_401);
    }

    /**
     * 对象转换
     *
     * @param oAuth2AccessToken
     * @return
     */
    private TokenResult getTokenResult(OAuth2AccessToken oAuth2AccessToken) {

        TokenResult userResult = new TokenResult();
        if (oAuth2AccessToken == null) {
            return userResult;
        }
        userResult.setAccessToken(oAuth2AccessToken.getValue());
        userResult.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
        userResult.setExpiresIn(oAuth2AccessToken.getExpiresIn());
        return userResult;
    }


    /**
     * 移除Token
     *
     * @return
     */
    public boolean removeToken() {
        String accessTokenStr = request.getHeader("access-token");
        if (StringUtils.isEmpty(accessTokenStr)){
            return true;
        }
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenStr);
        if (accessToken != null) {
            // 移除access_token
            tokenStore.removeAccessToken(accessToken);
            redisAuthenticationCodeServices.remove(accessTokenStr);
            redisAuthenticationCodeServices.removeAuthority(accessTokenStr);
            // 移除refresh_token
            if (accessToken.getRefreshToken() != null) {
                tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }
        }
        return true;
    }

    public OAuth2AccessToken refreshAccessToken(String refreshAccessToken, String accessToken) {

        String clientId = "clientId";
        String clientSecret = "secretId";
        String grantType = "refresh_token";
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("grant_type", grantType);
        requestParameters.put("client_id", clientId);
        requestParameters.put("client_secret", clientSecret);

        Set<String> scopes = new HashSet<>();
        scopes.add("all");
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientId, scopes, grantType);

        tokenStore.removeAccessToken(accessToken);

        OAuth2AccessToken oAuth2AccessToken = defaultTokenServices.refreshAccessToken(refreshAccessToken, tokenRequest);

        return oAuth2AccessToken;
    }

}
