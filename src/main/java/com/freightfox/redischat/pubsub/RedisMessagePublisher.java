package com.freightfox.redischat.pubsub;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisMessagePublisher(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void publish(String roomId, Object message){
        String channel = "room_channel:" + roomId;
        redisTemplate.convertAndSend(channel, message);
    }
}
