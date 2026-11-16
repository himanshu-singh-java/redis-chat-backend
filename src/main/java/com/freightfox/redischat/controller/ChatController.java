package com.freightfox.redischat.controller;

import com.freightfox.redischat.dto.ApiResponse;
import com.freightfox.redischat.dto.JoinRequest;
import com.freightfox.redischat.dto.MessageRequest;
import com.freightfox.redischat.dto.RoomRequest;
import com.freightfox.redischat.model.ChatMessage;
import com.freightfox.redischat.service.ChatRoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatapp/chatrooms")
public class ChatController {

    private final ChatRoomService chatRoomService;

    public ChatController(ChatRoomService chatRoomService){
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@Valid @RequestBody RoomRequest request){
        chatRoomService.createRoom(request.getRoomName());
        return ResponseEntity.ok(Map.of(
                "message", "Chat room '" + request.getRoomName() + "' created successfully.",
                "roomId", request.getRoomName(),
                "status", "success"
        ));
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId,
                                                        @Valid @RequestBody JoinRequest joinRequest
                                                        ){
        chatRoomService.joinRoom(roomId, joinRequest.getParticipant());
        return ResponseEntity.ok(Map.of(
                "message", "User '" + joinRequest.getParticipant() + "' joined chat room '" + roomId + "'.",
                "status", "success"
        ));
    }

    @PostMapping("/{roomId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable String roomId,
                                         @Valid @RequestBody MessageRequest request){
        chatRoomService.sendMessage(roomId, request);
        return ResponseEntity.ok(Map.of(
                "message", "Message sent successfully.",
                "status", "success"
        ));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable String roomId,
                                         @RequestParam(defaultValue = "50") long limit){
        List<ChatMessage> messages = chatRoomService.getMessages(roomId, limit);
        return ResponseEntity.ok(Map.of("messages", messages));
    }

}
