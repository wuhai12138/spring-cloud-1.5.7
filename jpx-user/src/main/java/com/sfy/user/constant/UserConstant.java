package com.sfy.user.constant;

import java.util.Random;
import java.util.stream.DoubleStream;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
public class UserConstant {

    public static final int Redis_TimeOut = 5 * 60;//默认5分钟

    public static final String Role_Meuns_All_Key = "role_meuns_*";
    /**
     * redis中角色菜单Key
     */
    private static final String Role_Meuns_Key = "role_meuns_%d";
    /**
     * 角色权限Key
     */
    private static final String Role_Authorities_Key = "role_authorities_%d";

    /**
     * 图片验证码 redis key
     */
    public static final String Validate_Image_Redis_Key = "validate_image:";
    /**
     * 手机验证码 redis key
     */
    public static final String Validate_Mobile_Redis_Key = "validate_mobile:";

    /**
     * 获取角色菜单Key
     * @param roleId
     * @return
     */
    public static final String getRoleMeunsKey(Long roleId) {
        if (roleId == null) {
            roleId = 0L;
        }
        return String.format(Role_Meuns_Key, roleId);
    }

    /**
     * 获取角色权限Key
     * @param roleId
     * @return
     */
    public static final String getRoleAuthoritiesKey(Long roleId){
        if (roleId == null) {
            roleId = 0L;
        }
        return String.format(Role_Authorities_Key, roleId);
    }

    /**
     * 获取Redis Timeout
     * @return
     */
    public static final int getRedisTimeOut(){
        return getRedisTimeOut(9);
    }

    /**
     * ((10 + random) * Redis_TimeOut) / 10;
     * @param bound 1-10
     * @return
     */
    public static final int getRedisTimeOut(int bound) {

        bound = bound % 10;
        int random = new Random().nextInt(bound);
        if (random > 0) {
            return ((10 + random) * Redis_TimeOut) / 10;
        }
        return Redis_TimeOut;
    }
    
    
    //region//////////////////////////////SMS/////////////////////////////////////////



    
    
    //endregion //////////////////////////////SMS/////////////////////////////////////////

}
