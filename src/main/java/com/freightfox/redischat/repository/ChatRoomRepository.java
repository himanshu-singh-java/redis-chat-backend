package com.freightfox.redischat.repository;

import com.freightfox.redischat.model.ChatMessage;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Repository
public class ChatRoomRepository {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, ChatMessage> chatMessageRedisTemplate;
    private final JsonMapper jsonMapper;

    private static final String ROOM_METADATA_KEY = "chatrooms";
    private static final String PARTICIPANTS_KEY_PREFIX = "chatroom:%s:participants";
    private static final String MESSAGE_KEY_PREFIX = "chatroom:%s:messages";

    public ChatRoomRepository(StringRedisTemplate stringRedisTemplate,
                              RedisTemplate<String, ChatMessage> chatMessageRedisTemplate,
                              JsonMapper jsonMapper){
        this.stringRedisTemplate = stringRedisTemplate;
        this.chatMessageRedisTemplate = chatMessageRedisTemplate;
        this.jsonMapper = jsonMapper;
    }

    @SneakyThrows
    public boolean createRoomAutomatically(String roomId){
        Map<String, String> metadata = Map.of(
                "roomId", roomId,
                "roomName", roomId,
                "createdAt", Instant.now().toString()
        );
        String metadataJson = jsonMapper.writeValueAsString(metadata);
        return Boolean.TRUE.equals(stringRedisTemplate.opsForHash().putIfAbsent(ROOM_METADATA_KEY, roomId, metadataJson));
    }

    public boolean isParticipant(String roomId, String participant){
        String participantKey = String.format(PARTICIPANTS_KEY_PREFIX, roomId);
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(participantKey, participant));
    }

    public Boolean roomExists(String roomId){
        return stringRedisTemplate.opsForHash().hasKey(ROOM_METADATA_KEY, roomId);
    }

    public void addParticipant(String roomId, String participant){
        String participantKey = String.format(PARTICIPANTS_KEY_PREFIX, roomId);
        stringRedisTemplate.opsForSet().add(participantKey, participant);
    }

    public void saveMessage(String roomId, ChatMessage message){
        String messageKey = String.format(MESSAGE_KEY_PREFIX, roomId);
        chatMessageRedisTemplate.opsForList().rightPush(messageKey, message);
    }

    public List<ChatMessage> getMessages(String roomId, long limit){
        String messageKey = String.format(MESSAGE_KEY_PREFIX, roomId);
        long start = (limit > 0) ? -limit : 0;

        return chatMessageRedisTemplate.opsForList().range(messageKey, start, -1);
    }
}
