package com.aktic.indussahulatbackend.config;

import com.aktic.indussahulatbackend.service.redis.ExpirationListener;
import com.aktic.indussahulatbackend.service.redis.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisService redisService;

    @PostConstruct
    public void configureRedisNotifications() {
        log.info("Attempting to configure Redis notify-keyspace-events...");
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            connection.setConfig("notify-keyspace-events", "Ex");
            log.info("Redis notify-keyspace-events set to 'Ex'");
        } catch (Exception e) {
            log.error("Failed to configure Redis notify-keyspace-events using RedisConnection.", e);
        }
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(mainExpirationListener(), new ChannelTopic("__keyevent@0__:expired"));
        log.info("RedisMessageListenerContainer configured to listen on '__keyevent@0__:expired'");
        return container;
    }

    @Bean
    public MessageListenerAdapter mainExpirationListener() {
        log.info("Creating MainExpirationListener to delegate based on key patterns");
        return new MessageListenerAdapter(new ExpirationListener(redisService));
    }
}