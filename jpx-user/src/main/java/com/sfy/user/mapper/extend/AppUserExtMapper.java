package com.sfy.user.mapper.extend;

import com.sfy.user.mapper.provider.AppUserExtProvider;
import com.sfy.user.dto.user.AppUserDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
public interface AppUserExtMapper {

    @SelectProvider(type = AppUserExtProvider.class, method = "count")
    int count(@Param("search") Map<String, Object> search);

    @SelectProvider(type = AppUserExtProvider.class, method = "query")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "username", property = "username"),
            @Result(column = "first_name", property = "firstName"),
            @Result(column = "last_name", property = "lastName"),
            @Result(column = "email", property = "email"),
            @Result(column = "image_url", property = "imageUrl"),
            @Result(column = "enabled", property = "enabled"),
            @Result(column = "password_revised", property = "passwordRevised"),
            @Result(column = "user_code", property = "userCode"),
            @Result(column = "user_nickname", property = "userNickname"),
            @Result(column = "user_icon", property = "userIcon"),
            @Result(column = "user_address", property = "userAddress"),
            @Result(column = "register_type", property = "registerType"),
            @Result(column = "register_mobile", property = "registerMobile"),
            @Result(column = "mobile_version", property = "mobileVersion"),
            @Result(column = "app_version", property = "appVersion"),
            @Result(column = "register_time", property = "registerTime"),
            @Result(column = "is_login", property = "isLogin"),
            @Result(column = "login_time", property = "loginTime"),
            @Result(column = "device_id", property = "deviceId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    List<AppUserDto> queryList(@Param("search") Map<String, Object> search, @Param("offset") int offset, @Param("limit") int limit, @Param("orderBy") String orderBy);
}
