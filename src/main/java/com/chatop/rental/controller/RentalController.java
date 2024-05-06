package com.chatop.rental.controller;

import com.chatop.rental.dto.MessageResponse;
import com.chatop.rental.dto.RentalRequest;
import com.chatop.rental.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;
    
    @Operation(summary = "Create a new rental",
            description = "Creates a new rental listing and returns a success message")
    @ApiResponse(responseCode = "200", description = "Rental created successfully",
              content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponse.class),
              examples = @ExampleObject(name = "Successful Response",
              value = "{\"message\": \"Rental created!\"}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized",
    content = @Content(mediaType = "application/json",
                       examples = @ExampleObject(name = "Empty response")))    
    @PostMapping
    public Optional<MessageResponse> createRental(@ModelAttribute @Valid RentalRequest rentalRequest, Authentication authentication) {
        return rentalService.createRental(rentalRequest, authentication.getName());
    }
}
