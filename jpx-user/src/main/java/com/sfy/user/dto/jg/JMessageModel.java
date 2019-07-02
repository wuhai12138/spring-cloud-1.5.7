package com.sfy.user.dto.jg;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by 金鹏祥 on 2019/5/20.
 */
@Data
public class JMessageModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
}
