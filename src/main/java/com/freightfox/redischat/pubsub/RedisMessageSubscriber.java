package com.freightfox.redischat.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {
    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());

        log.info("[REAL-TIME ALERT] Channel: " + channel + " | New Message: " + body);
    }
}
