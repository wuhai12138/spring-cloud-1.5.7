package com.sfy.user.dto.jg;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 金鹏祥
 * @date 2019/5/23 13:59
 * @description
 */
@Data
public class JGMessageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String phone;

    public JGMessageInfo(String phone) {
        this.phone = phone;
    }
}
