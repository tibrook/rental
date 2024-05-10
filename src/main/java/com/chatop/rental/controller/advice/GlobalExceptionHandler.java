package com.chatop.rental.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.chatop.rental.exception.AuthenticationException;
import com.chatop.rental.exception.BadRequestException;
import com.chatop.rental.exception.JwtAuthenticationException;
import com.chatop.rental.exception.UnAuthorizedException;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<Object> createResponse(String message, HttpStatus status) {
        if ("{}".equals(message)) {
            return ResponseEntity.status(status).body(new HashMap<>());
        } else if (message == null || message.isEmpty()) {
            return ResponseEntity.status(status).build();
        } else {
            return ResponseEntity.status(status).body(Collections.singletonMap("message", message));
        }
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        return createResponse("{}", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Object> handleUnAuthorizedException(UnAuthorizedException ex, HttpServletRequest request) {
        return createResponse("", HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationxception(AuthenticationException ex, HttpServletRequest request) {
        return createResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Object> handleJwtAuthenticationException(JwtAuthenticationException ex, HttpServletRequest request, WebRequest pathRequest) {
        String path = pathRequest.getDescription(false);
        String message = path.contains("/api/auth/me") ? new HashMap<>().toString() : "";
        return createResponse(message, HttpStatus.UNAUTHORIZED);
    }
}
