package com.freightfox.redischat.pubsub;

import com.freightfox.redischat.model.ChatMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher {

    private final RedisTemplate<String, ChatMessage> chatMessageRedisTemplate;

    public RedisMessagePublisher(RedisTemplate<String, ChatMessage> chatMessageRedisTemplate){
        this.chatMessageRedisTemplate = chatMessageRedisTemplate;
    }

    public void publish(String roomId, ChatMessage message){
       chatMessageRedisTemplate.convertAndSend("room_channel:" + roomId, message);
    }
}
