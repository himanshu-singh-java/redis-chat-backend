package com.freightfox.redischat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoomRequest {

    @NotBlank(message = "Room name cannot be empty")
    @Size(max = 50, message = "Room name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Room name can only contain letters, numbers, underscores, and hyphens")
    private String roomName;
}
