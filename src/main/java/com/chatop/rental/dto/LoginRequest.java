package com.chatop.rental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {
		
	@Email
    @NotEmpty
    private final String login;
    
    @NotEmpty
    @Size(min = 8)
    private final String password;
    
    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}

