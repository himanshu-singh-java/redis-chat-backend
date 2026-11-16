package com.freightfox.redischat.config;

import com.freightfox.redischat.pubsub.RedisMessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){

        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());

        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisMessageSubscriber messageSubscriber){
        return new MessageListenerAdapter(messageSubscriber);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
                                         RedisConnectionFactory connectionFactory,
                                         MessageListenerAdapter messageListenerAdapter
                                         ){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(messageListenerAdapter, new PatternTopic("room_channel:*"));

        return container;
    }
}
