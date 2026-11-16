package com.freightfox.redischat.mapper;

import com.freightfox.redischat.dto.MessageRequest;
import com.freightfox.redischat.model.ChatMessage;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ChatMapper {
    public ChatMessage toChatMessage(MessageRequest messageRequest){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setParticipant(messageRequest.getParticipant());
        chatMessage.setMessage(messageRequest.getMessage());
        chatMessage.setTimeStamp(Instant.now().toString());
        return chatMessage;
    }
}
