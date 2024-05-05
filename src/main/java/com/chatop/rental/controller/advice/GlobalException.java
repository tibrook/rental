//package com.chatop.rental.controller.advice;
//
//import org.springframework.http.HttpStatus;
//
//public class GlobalException extends RuntimeException {
//    private static final long serialVersionUID = 1L;  
//    private HttpStatus status;
//    private String message;
//
//    public GlobalException(HttpStatus status, String message) {
//        super(message);
//        this.status = status;
//        this.message = message;
//    }
//
//    public HttpStatus getStatus() {
//        return status;
//    }
//
//    @Override
//    public String getMessage() {
//        return message;
//    }
//}
