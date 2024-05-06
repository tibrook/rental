package com.chatop.rental.controller;

import com.chatop.rental.dto.MessageResponse;
import com.chatop.rental.dto.RentalRequest;
import com.chatop.rental.service.RentalService;

import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;
    
    @PostMapping
    public Optional<MessageResponse> createRental(@ModelAttribute @Valid RentalRequest rentalRequest, Authentication authentication) {
        return rentalService.createRental(rentalRequest, authentication.getName());
    }
}
