package com.chatop.rental.controller;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.exception.UnAuthorizedException;
import com.chatop.rental.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @Operation(summary = "Get user by ID", description = "Fetches details of a user by their ID")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Successfully retrieved user details",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = UserDto.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized",
    	content = @Content(mediaType = "application/json",
                           examples = @ExampleObject(name = "Bad or missing token"))) 
    })
    @GetMapping("/{id}")
    public Optional<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        // Handle the case of user not found here because userService.getUserById is also used by messageService
        // And BadRequestException is needed in messageService
        if(user.isEmpty()) {
            log.error("No user found with ID: {}", id);
        	throw new UnAuthorizedException();
        }
    	return user;
    }

}
