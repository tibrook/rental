package com.chatop.rental.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {
		
	@Email
    @NotEmpty
    private final String email;
    
    @NotEmpty
    @Size(min = 8)
    private final String password;
    
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

