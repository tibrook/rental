package com.chatop.rental.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageRequest {
    @NotNull(message = "Message cannot be null")
    @Size(min = 1, max = 2000, message = "Message cannot be empty")
    private String message;
    
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be greater than 0")
    @JsonProperty("user_id")
    private Integer userId;
    
    @NotNull(message = "Rental ID is required")
    @Min(value = 1, message = "Rental ID must be greater than 0")
    @JsonProperty("rental_id")
    private Integer rentalId;
}