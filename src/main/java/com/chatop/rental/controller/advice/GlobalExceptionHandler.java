package com.chatop.rental.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Object> createResponseBasedOnPath(String path, HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        if (message != null && !message.isEmpty() && message != "{}") {
            body.put("message", message);
        }

        return ResponseEntity.status(status).body(message == "{}" ? message  : body.isEmpty() ? null : body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralExceptions(Exception ex, WebRequest request) {
        logger.error("Exception handled: {}", request.getDescription(false), ex);

        String path = request.getDescription(false);
        if (path.contains("/api/auth/register")) {
            return createResponseBasedOnPath(path, HttpStatus.BAD_REQUEST, "{}");
        } else if (path.contains("/api/auth/login")) {
            return createResponseBasedOnPath(path, HttpStatus.UNAUTHORIZED, "error");
        } else if (path.contains("/api/auth/me")) {
            return createResponseBasedOnPath(path, HttpStatus.UNAUTHORIZED, "{}");
        } else if (path.contains("/api/messages")) {
            return createResponseBasedOnPath(path, HttpStatus.BAD_REQUEST, "{}");
        } else {
            return createResponseBasedOnPath(path, HttpStatus.UNAUTHORIZED, null);
        }
    } 
}
