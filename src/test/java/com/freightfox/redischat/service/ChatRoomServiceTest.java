package com.freightfox.redischat.service;

import com.freightfox.redischat.dto.MessageRequest;
import com.freightfox.redischat.exception.ChatException;
import com.freightfox.redischat.mapper.ChatMapper;
import com.freightfox.redischat.model.ChatMessage;
import com.freightfox.redischat.pubsub.RedisMessagePublisher;
import com.freightfox.redischat.repository.ChatRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private RedisMessagePublisher redisMessagePublisher;

    @Mock
    private ChatMapper chatMapper;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Test
    void testCreateRoom_Success(){
        String roomId = "SafarX-Room";

        when(chatRoomRepository.createRoomAutomatically(roomId)).thenReturn(true);

        chatRoomService.createRoom(roomId);

        verify(chatRoomRepository, times(1)).createRoomAutomatically(roomId);
    }

    @Test
    void testCreateRoom_AlreadyExists_ThrowException(){
        String roomId = "SafarX-Room";

        when(chatRoomRepository.createRoomAutomatically(roomId)).thenReturn(false);

        ChatException chatException = assertThrows(ChatException.class, ()->{
            chatRoomService.createRoom(roomId);
        });

        assertEquals("Room already Exists", chatException.getMessage());

        verify(chatRoomRepository, times(1)).createRoomAutomatically(roomId);
    }

    @Test
    void testJoinRoom_Success(){
        String roomId = "SafarX-Room";
        String participant = "Himanshu";

        when(chatRoomRepository.roomExists(roomId)).thenReturn(true);

        chatRoomService.joinRoom(roomId, participant);
        verify(chatRoomRepository, times(1)).addParticipant(roomId, participant);
    }

    @Test
    void testJoinRoom_RoomDoesNotExist_ThrowException(){
        String roomId = "SafarX-Room";
        String participant = "Himanshu";

        when(chatRoomRepository.roomExists(roomId)).thenReturn(false);

        ChatException chatException = assertThrows(ChatException.class, ()-> {
            chatRoomService.joinRoom(roomId, participant);
        });

        assertEquals("Room does not exist!", chatException.getMessage());

        verify(chatRoomRepository, never()).addParticipant(roomId, participant);
    }

    @Test
    void testSendMessage_Success(){
        String roomId = "SafarX-Room";
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setParticipant("Himanshu");
        messageRequest.setMessage("Hello testing");

        ChatMessage dummyMessage = new ChatMessage("Himanshu", "Hello Testing", Instant.now().toString());

        when(chatRoomRepository.roomExists(roomId)).thenReturn(true);

        when(chatRoomRepository.isParticipant(roomId, messageRequest.getParticipant())).thenReturn(true);

        when(chatMapper.toChatMessage(any(MessageRequest.class))).thenReturn(dummyMessage);

        chatRoomService.sendMessage(roomId, messageRequest);

        verify(chatRoomRepository, times(1)).saveMessage(eq(roomId), any(ChatMessage.class));
        verify(redisMessagePublisher, times(1)).publish(eq(roomId), any(ChatMessage.class));
    }

    @Test
    void testGetMessages_Success(){
        String roomId = "SafarX-Room";
        long limit = 10;

        ChatMessage msg1 = new ChatMessage("Alok", "Hi", Instant.now().toString());
        ChatMessage msg2 = new ChatMessage("Himanshu", "Hello", Instant.now().toString());

        List<Object> mockMessages = List.of(msg1, msg2);

        when(chatRoomRepository.roomExists(roomId)).thenReturn(true);

        when(chatRoomRepository.getMessages(roomId, limit)).thenReturn(mockMessages);

        List<ChatMessage> result = chatRoomService.getMessages(roomId, limit);

        assertEquals(2, result.size());
        assertEquals("Alok", result.get(0).getParticipant());
        verify(chatRoomRepository, times(1)).getMessages(roomId, limit);
    }
}
