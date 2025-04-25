package com.aktic.indussahulatbackend.config;

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
    private final RedisService redisService; // Assuming RedisService is correctly injected

    @PostConstruct
    public void configureRedisNotifications() {
        log.info("Attempting to configure Redis notify-keyspace-events...");
        // Use try-with-resources to ensure the connection is closed
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            // Use the standard RedisConnection interface to set configuration
            connection.setConfig("notify-keyspace-events", "Ex"); // E = Key events, x = Expired events
            log.info("Redis notify-keyspace-events set to 'Ex'");
        } catch (Exception e) {
            // Log the error more specifically
            log.error("Failed to configure Redis notify-keyspace-events using RedisConnection. Ensure Redis server is running and accessible.", e);
        }
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        // Listen specifically to events in database 0 for expired keys
        container.addMessageListener(messageListenerAdapter(), new ChannelTopic("__keyevent@0__:expired"));
        log.info("RedisMessageListenerContainer configured to listen on '__keyevent@0__:expired'");
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        log.info("Creating MessageListenerAdapter to delegate expired events to RedisService.handleEventAmbulanceAssignmentExpiration");
        return new MessageListenerAdapter(redisService, "handleEventAmbulanceAssignmentExpiration");
    }
}