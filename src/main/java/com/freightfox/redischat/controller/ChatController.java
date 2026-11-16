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

@RestController
@RequestMapping("/api/chatapp/chatrooms")
public class ChatController {

    private final ChatRoomService chatRoomService;

    public ChatController(ChatRoomService chatRoomService){
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createRoom(@Valid @RequestBody RoomRequest request){
        chatRoomService.createRoom(request.getRoomName());
        return ResponseEntity.ok(ApiResponse.success("Room created successfully", request.getRoomName(), null));
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<ApiResponse<String>> joinRoom(@PathVariable String roomId,
                                                        @Valid @RequestBody JoinRequest joinRequest
                                                        ){
        chatRoomService.joinRoom(roomId, joinRequest.getParticipant());
        return ResponseEntity.ok(ApiResponse.success(joinRequest.getParticipant() + " joined the room", roomId, null));
    }

    @PostMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse<String>> sendMessage(@PathVariable String roomId,
                                                           @Valid @RequestBody MessageRequest request){
        chatRoomService.sendMessage(roomId, request);
        return ResponseEntity.ok(ApiResponse.success("Message sent successfully", roomId, null));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessage>>> getMessages(@PathVariable String roomId,
                                                           @RequestParam(defaultValue = "50") long limit){
        List<ChatMessage> messages = chatRoomService.getMessages(roomId, limit);
        return ResponseEntity.ok(ApiResponse.success("Chat history fetched", roomId, messages));
    }

}
