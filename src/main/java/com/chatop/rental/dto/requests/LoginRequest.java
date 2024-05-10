package com.chatop.rental.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {
		
	@Email(message = "Email should be in email format.")
	@NotEmpty(message = "Email cannot be empty.")
    private final String email;
    
	@NotEmpty(message = "Password cannot be empty.")
	@Size(min = 8, message = "Password must be at least 8 characters long.")
    private final String password;
    
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

