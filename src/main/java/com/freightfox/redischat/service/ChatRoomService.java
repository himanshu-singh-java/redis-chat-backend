package com.freightfox.redischat.service;

import com.freightfox.redischat.dto.MessageRequest;
import com.freightfox.redischat.exception.ChatException;
import com.freightfox.redischat.mapper.ChatMapper;
import com.freightfox.redischat.model.ChatMessage;
import com.freightfox.redischat.pubsub.RedisMessagePublisher;
import com.freightfox.redischat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    private final ChatMapper chatMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisMessagePublisher redisMessagePublisher;

    public ChatRoomService(ChatRoomRepository chatRoomRepository,
                           RedisMessagePublisher messagePublisher,
                           ChatMapper chatMapper){
        this.chatRoomRepository = chatRoomRepository;
        this.redisMessagePublisher = messagePublisher;
        this.chatMapper = chatMapper;
    }

    public void createRoom(String roomId){
        if(chatRoomRepository.roomExists(roomId)){
            throw new ChatException("Room already Exists");
        }

        chatRoomRepository.createRoom(roomId);
    }

    public void joinRoom(String roomId, String participant) {
        if(!chatRoomRepository.roomExists(roomId)){
            throw new ChatException("Room does not exist!");
        }

        chatRoomRepository.addParticipant(roomId, participant);
    }


    public void sendMessage(String roomId, MessageRequest request) {
        if(!chatRoomRepository.roomExists(roomId)){
            throw new ChatException("Room does not exist!");
        }

        ChatMessage message = chatMapper.toChatMessage(request);

        chatRoomRepository.saveMessage(roomId, message);
        redisMessagePublisher.publish(roomId, message);
    }

    public List<ChatMessage> getMessages(String roomId, long limit){
        if(!chatRoomRepository.roomExists(roomId)){
            throw new ChatException("Room does not exist!");
        }

        List<Object> messages = chatRoomRepository.getMessages(roomId, limit);
        return messages.stream().map(msg -> (ChatMessage) msg).collect(Collectors.toList());
    }
}
