package com.sfy.user.dto.user;

/**
 * @author 金鹏祥
 * @date 2019/5/29 9:37
 * @description
 */
public enum UserAgentEnum {

    PAD(1,"小方元"),
    SYS(2,"系统用户"),
    APP(3,"app用户");

    private final int code;
    private final String desc;

    UserAgentEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
