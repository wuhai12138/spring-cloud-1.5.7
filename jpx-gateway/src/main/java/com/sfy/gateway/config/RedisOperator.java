package com.sfy.gateway.config;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisOperator {
    public RedisTemplate getRedisTemplate(RedisTemplate redisTemplate, int index) {
        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
        jedisConnectionFactory.setDatabase(index);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }
}