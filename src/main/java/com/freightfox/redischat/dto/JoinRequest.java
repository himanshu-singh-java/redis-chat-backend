package com.freightfox.redischat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinRequest {

    @NotBlank(message = "Participant name cannot be Empty")
    @Size(max = 50, message = "Participant name must not exceed 50 characters")
    private String participant;
}
