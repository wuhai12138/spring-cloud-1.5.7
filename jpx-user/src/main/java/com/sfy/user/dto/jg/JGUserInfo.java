package com.sfy.user.dto.jg;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 金鹏祥
 * @date 2019/5/16 16:13
 * @description
 */
@Data
public class JGUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String phone;
    private String nickName;
    private String avatar;
    private Long updateTime;

    public JGUserInfo(String phone, String nickName, String avatar, Long updateTime) {
        this.phone = phone;
        this.nickName = nickName;
        this.avatar = avatar;
        this.updateTime = updateTime;
    }
}
