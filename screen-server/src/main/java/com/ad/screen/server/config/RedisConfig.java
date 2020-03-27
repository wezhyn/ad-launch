package com.ad.screen.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
@Configuration
@EnableTransactionManagement
public class RedisConfig {


    @Bean(name="IntegerRedisTemplate")
    public RedisTemplate<Integer, Integer> getRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Integer, Integer> template=new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new GenericToStringSerializer<>(Integer.class));
        template.setHashKeySerializer(new GenericToStringSerializer<>(Integer.class));
        template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        template.setHashValueSerializer(new GenericToStringSerializer<>(Integer.class));
        template.setEnableTransactionSupport(true);
        return template;
    }
}
