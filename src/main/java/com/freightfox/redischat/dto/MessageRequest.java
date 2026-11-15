package com.freightfox.redischat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequest {

    @NotBlank(message = "Participant name cannot be empty")
    private String participant;

    @NotBlank(message = "Message cannot be empty")
    private String message;
}
