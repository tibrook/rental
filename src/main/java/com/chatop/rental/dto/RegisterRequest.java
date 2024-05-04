package com.chatop.rental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
	
	@Email
    @NotEmpty
    private String email;
    
    @Size(min = 8)
    @NotEmpty
    private String password;
    
    @NotBlank
    private String name;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}