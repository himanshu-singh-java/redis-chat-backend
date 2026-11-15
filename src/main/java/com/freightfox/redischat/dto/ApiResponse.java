package com.freightfox.redischat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T>{

    private String status;
    private String message;
    private String roomId;
    private T data;

    public static <T> ApiResponse<T> success(String message, String roomId, T data){
        return new ApiResponse<>("success", message, roomId, data);
    }

    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<>("error", message, null, null);
    }
}
