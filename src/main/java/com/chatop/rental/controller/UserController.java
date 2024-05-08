package com.chatop.rental.controller;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Operation(summary = "Get user by ID", description = "Fetches details of a user by their ID")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Successfully retrieved user details",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = UserDto.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized",
    	content = @Content(mediaType = "application/json",
                           examples = @ExampleObject(name = "Empty response"))) 
    })
    @GetMapping("/{id}")
    public Optional<UserDto> getUserById(@PathVariable Long id) {
    	return userService.getUserById(id);
    }

}
