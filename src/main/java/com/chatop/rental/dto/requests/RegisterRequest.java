package com.chatop.rental.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegisterRequest {
	
	@Email(message = "Email should be in email format")
	@NotEmpty(message = "Email cannot be empty.")
	@Size(max = 255, message = "Email must not exceed 255 characters.")
    private final String email;
    
	@NotEmpty(message = "Password cannot be empty.")
	@Size(min = 8, message = "Password must be at least 8 characters long.")
    private final String password;
    
	@NotBlank(message = "Name is required and cannot be blank.")
    @Size(max = 255, message = "Name must not exceed 255 characters.")
    private final String name;

}