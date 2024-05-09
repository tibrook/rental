package com.chatop.rental.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor 
@Getter 
@Data
public class ErrorResponse {
    private String message;
}