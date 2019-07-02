package com.sfy.user.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by 金鹏祥 on 2019/6/18.
 */
@Data
public class TestUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer Id;
    private String name;
}
