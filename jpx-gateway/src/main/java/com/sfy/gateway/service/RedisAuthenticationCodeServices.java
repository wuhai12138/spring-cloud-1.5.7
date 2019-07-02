package com.sfy.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

import java.util.List;

@Service
@Slf4j
public class RedisAuthenticationCodeServices extends RandomValueAuthorizationCodeServices {
    private static final String AUTH_CODE_KEY = "auth_code";
    private RedisConnectionFactory connectionFactory;
    @Autowired
    protected RedisTemplate redisTemplate;

    public RedisAuthenticationCodeServices(RedisConnectionFactory connectionFactory) {
        Assert.notNull(connectionFactory, "RedisConnectionFactory required");
        this.connectionFactory = connectionFactory;
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        RedisConnection conn = getConnection();
        try {
            OAuth2Authentication authentication = null;

            try {
                if (null != conn.hGet(AUTH_CODE_KEY.getBytes("utf-8"),
                        code.getBytes("utf-8"))) {
                    conn.hDel(AUTH_CODE_KEY.getBytes("utf-8"), code.getBytes("utf-8"));
                }
            } catch (Exception e) {
                return null;
            }

            return null;
        } catch (Exception e) {
            return null;
        } finally {
            conn.close();
        }
    }

    public void storeMy(String code, PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken) {
        RedisConnection conn = getConnection();
        try {
            conn.hSet(AUTH_CODE_KEY.getBytes("utf-8"), code.getBytes("utf-8"),
                    SerializationUtils.serialize(preAuthenticatedAuthenticationToken));
        } catch (Exception e) {
            log.error("保存authentication code 失败", e);
        } finally {
            conn.close();
        }
    }

    public List<String> getAuthorityList(String code) {
        RedisConnection conn = getConnection();
        try {
            List<String> list = null;

            try {
                list = org.springframework.security.oauth2.common.util.SerializationUtils
                        .deserialize(conn.hGet("authority".getBytes("utf-8"), code.getBytes("utf-8")));
            } catch (Exception e) {
                return null;
            }
            return list;
        } catch (Exception e) {
            return null;
        } finally {
            conn.close();
        }
    }

    public PreAuthenticatedAuthenticationToken getMy(String code) {
        RedisConnection conn = getConnection();
        try {
            PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken = null;

            try {
                preAuthenticatedAuthenticationToken = org.springframework.security.oauth2.common.util.SerializationUtils
                        .deserialize(conn.hGet(AUTH_CODE_KEY.getBytes("utf-8"),
                                code.getBytes("utf-8")));
            } catch (Exception e) {
                return null;
            }
            return preAuthenticatedAuthenticationToken;
        } catch (Exception e) {
            return null;
        } finally {
            conn.close();
        }
    }

    public OAuth2Authentication get(String code) {
        RedisConnection conn = getConnection();
        try {
            OAuth2Authentication authentication = null;

            try {
                authentication = org.springframework.security.oauth2.common.util.SerializationUtils
                        .deserialize(conn.hGet(AUTH_CODE_KEY.getBytes("utf-8"),
                                code.getBytes("utf-8")));
            } catch (Exception e) {
                return null;
            }
            return authentication;
        } catch (Exception e) {
            return null;
        } finally {
            conn.close();
        }
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        RedisConnection conn = getConnection();
        try {
            conn.hSet(AUTH_CODE_KEY.getBytes("utf-8"), code.getBytes("utf-8"),
                    SerializationUtils.serialize(authentication));
        } catch (Exception e) {
            log.error("保存authentication code 失败", e);
        } finally {
            conn.close();
        }
    }

    private RedisConnection getConnection() {
        return connectionFactory.getConnection();
    }

}