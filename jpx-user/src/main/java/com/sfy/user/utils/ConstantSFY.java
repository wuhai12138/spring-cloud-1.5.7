package com.sfy.user.utils;

import org.springframework.data.redis.core.RedisTemplate;

public class ConstantSFY {
//    public final static int CODE_401 = 401;
//    public final static String MESSAGE_401 = "令牌错误";
//    public final static int CODE_500 = 500;
//    public final static String MESSAGE_500 = "异常";
//    public final static int CODE_400 = 400;
//    public final static String MESSAGE_400 = "处理失败";
//    public final static int CODE_500 = 500;
//    public final static String MESSAGE_500 = "异常";
//    public final static int CODE_REGISTER_4002 = 4002;
//    public final static String MESSAGE_REGISTER_4002 = "用户名已存在";
//    public final static int CODE_LOGIN_4001 = 4001;
//    public final static String MESSAGE_LOGIN_4001 = "用户名密码错误";
//    public final static int CODE_GET_TOKEN_4002 = 4002;
//    public final static String MESSAGE_GET_TOKEN_4002 = "用户未登录";
//    public final static int CODE = 4002;

    public static RedisTemplate REDIS_TEMPLATE = null;
    public final static String APP_KEY = "98c89d750bd1cb10d4b45f6d";
    public final static String MASTER_SECRET = "7e5bb0a03b71c663fffe425e";

    public final static String PASSWORD_SALT = "";
    public final static String INIT_PASSWORD = "123456";
    public final static String IOS = "ios";
    public final static String ANDROID = "android";
    public final static String WEB = "web";

    public final static String USER_NICK_NAME = "nickName";
    public final static String USER_ICON = "icon";
    public final static String IS_TRUE = "1";
    public final static String IS_FALSE = "0";

    public static String SERVER_IP = "";
    public static int SERVER_PORT = 0;
    public static String SERVER_LOGIN_NAME = "";
    public static String SERVER_PASSWORD = "";
    public static long ICON_LONG_SIZE = 0L;
    public static String USER_CODE_TOP = "sfy";
    public static String INIT_T_X = "/init/images/initx.png";

    public final static int CODE_400 = 400;
    public final static String MESSAGE_400 = "处理失败";
    public final static int CODE_401 = 401;
    public final static String MESSAGE_401 = "令牌错误";
    public final static int CODE_403 = 403;
    public final static String MESSAGE_403 = "权限不足";
    public final static int CODE_500 = 500;
    public final static String MESSAGE_500 = "异常";

    public final static int CODE_1006 = 1006;
    public final static String MESSAGE_10006 = "feign断开连接";
    public final static int CODE_4001 = 4001;
    public final static String MESSAGE_4001 = "验证码错误";
    public final static int CODE_4002 = 4002;
    public final static String MESSAGE_4002 = "验证码错误";
    public final static int CODE_4003 = 4003;
    public final static String MESSAGE_4003 = "手机号不存在";
    public final static int CODE_4004 = 4004;
    public final static String MESSAGE_4004 = "用户名密码错误";
    public final static int CODE_4005 = 4005;
    public final static String MESSAGE_4005 = "新密码、确认密码不一致";
    public final static int CODE_4006 = 4006;
    public final static String MESSAGE_4006 = "验证码发送失败";
    public final static int CODE_4007 = 4007;
    public final static String MESSAGE_4007 = "用户已存在";
    public final static int CODE_4008 = 4008;
    public final static String MESSAGE_4008 = "用户未登录";
    public final static int CODE_4009 = 4009;
    public final static String MESSAGE_4009 = "类型错误";
    public final static int CODE_4010 = 4010;
    public final static String MESSAGE_4010 = "账号已被登录";
    public final static int CODE_4011 = 4011;
    public final static String MESSAGE_4011 = "账号已被登录";
    public final static int CODE_4023 = 4023;
    public final static int CODE_4012 = 4012;
    public final static String MESSAGE_4012 = "header{device-id不能为空}";
    public final static int CODE_4013 = 4013;
    public final static String MESSAGE_4013 = "header{phone不能为空}";
    public static final int CODE_204 = 204;
    public final static String MESSAGE_204 = "查询不到数据";
    public final static int CODE_4000 = 4000;
    public final static String MESSAGE_4000 = "查询不到内容";
}
