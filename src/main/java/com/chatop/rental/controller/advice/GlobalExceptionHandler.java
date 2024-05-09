package com.chatop.rental.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Object> createResponse(String message, HttpStatus status) {
        if ("{}".equals(message)) {
            return ResponseEntity.status(status).body(new HashMap<>());
        } else if (message == null || message.isEmpty()) {
            return ResponseEntity.status(status).build();
        } else {
            return ResponseEntity.status(status).body(Collections.singletonMap("message", message));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralExceptions(Exception ex, WebRequest request) {
        logger.error("Exception handled: {}", request.getDescription(false), ex);

        String path = request.getDescription(false);
        HttpStatus status;
        String message;

        if (path.contains("/api/auth/register") || path.contains("/api/messages")) {
            status = HttpStatus.BAD_REQUEST;
            message = "{}";
        } else if (path.contains("/api/auth/login")) {
            status = HttpStatus.UNAUTHORIZED;
            message = "error";
        } else if (path.contains("/api/auth/me")) {
            status = HttpStatus.UNAUTHORIZED;
            message = "{}";
        } else {
            status = HttpStatus.UNAUTHORIZED;
            message = null;
        }

        return createResponse(message, status);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Object> handleJwtAuthenticationException(JwtAuthenticationException ex, HttpServletRequest request, WebRequest pathRequest) {
        String path = pathRequest.getDescription(false);
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = path.contains("/api/auth/me") ? new HashMap<>().toString() : "";

        return createResponse(message, status);
    }
}
