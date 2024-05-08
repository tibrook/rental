package com.chatop.rental.controller;

import com.chatop.rental.dto.requests.MessageRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.service.interfaces.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Operation(summary = "Send a new message", description = "Stores a new message and returns a success message")
    @ApiResponse(responseCode = "200", description = "Message sent successfully",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = MessageResponse.class),  examples = @ExampleObject(value = "{\"message\": \"Message send with success\"}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized",
	content = @Content(mediaType = "application/json",
               examples = @ExampleObject(name = "Empty response")))
    @ApiResponse(responseCode = "400", description = "Bad Request (validation failed)",
    content = @Content(mediaType = "application/json",
                       examples = @ExampleObject(name = "Empty object", value = "{}"))) 
    @PostMapping
    public MessageResponse sendMessage(@RequestBody @Valid MessageRequest messageRequest) {
        return messageService.sendMessage(messageRequest);
    }
}
