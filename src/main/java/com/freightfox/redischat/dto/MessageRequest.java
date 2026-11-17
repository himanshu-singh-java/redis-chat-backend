package com.freightfox.redischat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageRequest {

    @NotBlank(message = "Participant name cannot be empty")
    @Size(max = 50, message = "Participant name must not exceed 50 characters")
    private String participant;

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 500, message = "Message must not exceed 500 characters")
    private String message;
}
