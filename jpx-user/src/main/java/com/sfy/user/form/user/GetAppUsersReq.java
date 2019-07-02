package com.sfy.user.form.user;

import com.sfy.user.form.PageReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Data
@ApiModel("获取App用户分页列表")
public class GetAppUsersReq extends PageReq {

    private Long id;
    private String username;
    private String email;
    private Byte enabled;
    private String userNickname;
    private String userCode;
    private String registerType;
    private Long regBeginTime;
    private Long regEndTime;

}
