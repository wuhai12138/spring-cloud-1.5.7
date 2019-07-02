package com.sfy.quartz.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserResult {
    private String asscessToken;
    private String refreshToken;
    private String tokenType;
    private boolean isExpired;
    private Date expiration;
    private int expiresIn;
    private String value;
}
