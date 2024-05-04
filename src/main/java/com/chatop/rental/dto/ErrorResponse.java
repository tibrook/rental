package com.chatop.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor 
@Getter 
public class ErrorResponse {
    private final String message;
}