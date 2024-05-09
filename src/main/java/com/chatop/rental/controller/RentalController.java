package com.chatop.rental.controller;

import com.chatop.rental.dto.RentalDto;
import com.chatop.rental.dto.requests.CreateRentalRequest;
import com.chatop.rental.dto.requests.UpdateRentalRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.dto.responses.RentalDetailResponse;
import com.chatop.rental.dto.responses.RentalListResponse;
import com.chatop.rental.service.interfaces.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;
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
    public Optional<MessageResponse> createRental(@ModelAttribute @Valid CreateRentalRequest rentalRequest, Authentication authentication) {
        return rentalService.createRental(rentalRequest, authentication.getName());
    }
    
    @Operation(
    		summary = "Get all rentals", 
    		description = "Returns a list of all rentals"
    )
    @ApiResponse(
    		responseCode = "200", 
    		description = "Successfully retrieved all rentals",
    		content = @Content(mediaType = "application/json",
    						   schema= @Schema(implementation = RentalListResponse.class),
    						   examples = @ExampleObject(name="Successful Response",
    						   value = "{\"rentals\": [{\"id\": 1, \"name\": \"dream house\", \"surface\": 24, \"price\": 30, \"picture\": \"https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg\", \"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.\", \"owner_id\": 1, \"created_at\": \"2012/12/02\", \"updated_at\": \"2014/12/02\"}]}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized",
			    content = @Content(mediaType = "application/json",
			                       examples = @ExampleObject(name = "Empty response")))
    @GetMapping
    public RentalListResponse getAllRentals() {
        List<RentalDto> rentals = rentalService.getAllRentals();
        return new RentalListResponse(rentals);
    }
    @Operation(
    		summary = "Get rental details by ID", 
            description = "Returns the detailed information of a specific rental by its ID")
    @ApiResponse(
    		responseCode = "200", 
    		description = "Successfully retrieved rental details", 
            content = @Content(mediaType = "application/json",
                               schema = @Schema(implementation = RentalDetailResponse.class),
                                 examples = @ExampleObject(name = "RentalDetailResponse", 
                                                           value = "{\"id\": 1, \"name\": \"dream house\", \"surface\": 24, \"price\": 30, \"picture\": [\"https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg\"], \"description\": \"Lorem ipsum dolor sit amet...\", \"owner_id\": 1, \"created_at\": \"2012/12/02\", \"updated_at\": \"2014/12/02\"}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized",
    		content = @Content(mediaType = "application/json",
                       examples = @ExampleObject(name = "Empty response")))
    @GetMapping("/{id}")
    public Optional<RentalDetailResponse>  getRentalById(@PathVariable Integer id) {
    	return rentalService.getRentalById(id);
    }
    
    @Operation(summary = "Update a rental", description = "Updates an existing rental and returns a success message")
    @ApiResponse(responseCode = "200", description = "Rental updated successfully",
                 content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Rental updated!\"}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized",
    		content = @Content(mediaType = "application/json",
                       examples = @ExampleObject(name = "Empty response")))
    @PutMapping("/{id}")
    public Optional<MessageResponse> updateRental(@PathVariable Integer id, @ModelAttribute @Valid UpdateRentalRequest rentalRequest, Authentication authentication) {
    	return rentalService.updateRental(id, rentalRequest, authentication);
    }
}
