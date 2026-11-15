package com.freightfox.redischat.repository;

import com.freightfox.redischat.model.ChatMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class ChatRoomRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ROOM_METADATA_KEY = "chatrooms";
    private static final String PARTICIPANTS_KEY_PREFIX = "chatroom:%s:participants";
    private static final String MESSAGE_KEY_PREFIX = "chatroom:%s:messages";

    public ChatRoomRepository(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void createRoom(String roomId){
        redisTemplate.opsForHash().put(ROOM_METADATA_KEY, roomId, Instant.now().toString());
    }

    public Boolean roomExists(String roomId){
        return redisTemplate.opsForHash().hasKey(ROOM_METADATA_KEY, roomId);
    }

    public void addParticipant(String roomId, String participant){
        String participantKey = String.format(PARTICIPANTS_KEY_PREFIX, roomId);
        redisTemplate.opsForSet().add(participantKey, participant);
    }

    public void saveMessage(String roomId, ChatMessage message){
        String messageKey = String.format(MESSAGE_KEY_PREFIX, roomId);
        redisTemplate.opsForList().rightPush(messageKey, message);
    }

    public List<Object> getMessages(String roomId, long limit){
        String messageKey = String.format(MESSAGE_KEY_PREFIX, roomId);
        long start = (limit > 0) ? -limit : 0;

        return redisTemplate.opsForList().range(messageKey, start, -1);
    }
}
