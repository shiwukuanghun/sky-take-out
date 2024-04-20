package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Auther: WuChen
 * @Date: 2024/4/18 23:40
 * @Description:
 **/
@Configuration
@Slf4j // 记录日志的
public class RedisConfiguration {
    @Bean // 把RedisConnectionFactory对象注入进来
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建redis模板对象。。。");
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置redis的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置redis的序列化器，不设置的话，在图形化界面看到的key就是乱码
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 给value设置序列化器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
