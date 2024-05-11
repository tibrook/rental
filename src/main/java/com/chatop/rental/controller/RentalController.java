package com.chatop.rental.controller;

import com.chatop.rental.dto.RentalDto;
import com.chatop.rental.dto.requests.CreateRentalRequest;
import com.chatop.rental.dto.requests.UpdateRentalRequest;
import com.chatop.rental.dto.responses.MessageResponse;
import com.chatop.rental.dto.responses.RentalDetailResponse;
import com.chatop.rental.dto.responses.RentalListResponse;
import com.chatop.rental.exception.BadRequestException;
import com.chatop.rental.exception.UnAuthorizedException;
import com.chatop.rental.service.interfaces.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.web.bind.annotation.ModelAttribute;

import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private static final Logger log = LoggerFactory.getLogger(RentalController.class);

    private RentalService rentalService;
    
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Operation(summary = "Create a new rental",
            description = "Creates a new rental listing and returns a success message. The request must include an image file.",
            requestBody = @RequestBody(content = 
            @Content(
                mediaType = "multipart/form-data",
                schema = @Schema(implementation = CreateRentalRequest.class),
                encoding = @Encoding(name = "picture", contentType = "image/jpeg, image/png"),
                examples = @ExampleObject(name = "CreateRentalExample",
                                          description = "Complete request with image file",
                                          value = "{\"name\": \"Charming House\", \"surface\": \"120.5\", \"price\": \"1500.00\", \"description\": \"Lovely two-bedroom house with a beautiful garden.\"}")
                ))
            )
    @ApiResponse(responseCode = "200", description = "Rental created successfully",
    content = @Content(mediaType = "application/json",
                       schema = @Schema(implementation = MessageResponse.class),
                       examples = @ExampleObject(name = "Successful Response",
                                                 value = "{\"message\": \"Rental created!\"}")))
	@ApiResponse(responseCode = "401", description = "Unauthorized",
	    content = @Content(mediaType = "application/json",
	                       examples = { @ExampleObject(name = "Invalid JWT"),
	                    		        @ExampleObject(name = "Validation error")
	                       }))

    @PostMapping
    public Optional<MessageResponse> createRental(@ModelAttribute @Valid CreateRentalRequest rentalRequest, BindingResult bindingResult,Authentication authentication) {
    	 log.info("Create Rental{}", rentalRequest.getName());
    	 if (bindingResult.hasErrors()) {
             String errorDetails = bindingResult.getFieldErrors().stream()
                 .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                 .collect(Collectors.joining(", "));
             log.error("Create rental : Validation failed for rental {}: {}", rentalRequest.getName(), errorDetails);
             throw new BadRequestException();
         }
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
			                       examples = @ExampleObject(name = "Invalid JWT")))
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
                       examples = { @ExampleObject(name = "Invalid JWT"),
               		        		@ExampleObject(name = "Rental not found")
                  }))
    @GetMapping("/{id}")
    public Optional<RentalDetailResponse>  getRentalById(@PathVariable Integer id) {
    	return rentalService.getRentalById(id);
    }
    @Operation(summary = "Update a rental",
            description = "Updates an existing rental and returns a success message.",
            requestBody = @RequestBody(content = 
	            @Content(
	                mediaType = "multipart/form-data",
	                schema = @Schema(implementation = UpdateRentalRequest.class),
	                examples = @ExampleObject(name = "UpdateRentalExample",
	                                          description = "Complete request",
	                                          value = "{\"name\": \"Charming House\", \"surface\": \"120.5\", \"price\": \"1500.00\", \"description\": \"Lovely two-bedroom house with a beautiful garden.\"}")
	                )
            	)
            )
    @ApiResponse(responseCode = "200", 
	    description = "Rental updated successfully",
	    content = @Content(mediaType = "application/json",
                       schema = @Schema(implementation = MessageResponse.class),
                       examples = @ExampleObject(value = "{\"message\": \"Rental updated!\"}")))
    @ApiResponse(responseCode = "401", 
	    description = "Unauthorized",
	    content = @Content(mediaType = "application/json",
                       examples = @ExampleObject(name = "Unauthorized", value = "{}")))
    @PutMapping("/{id}")
    public Optional<MessageResponse> updateRental(@PathVariable Integer id, @ModelAttribute @Valid UpdateRentalRequest rentalRequest,BindingResult bindingResult, Authentication authentication) {
    	if (bindingResult.hasErrors()) {
            String errorDetails = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
            log.error("Update rental : Validation failed {}", rentalRequest.getName(), errorDetails);
            throw new UnAuthorizedException();
        }
    	return rentalService.updateRental(id, rentalRequest, authentication);
    }
}
