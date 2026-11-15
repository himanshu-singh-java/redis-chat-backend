package com.freightfox.redischat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinRequest {

    @NotBlank(message = "Participant name cannot be Empty")
    private String participant;
}
