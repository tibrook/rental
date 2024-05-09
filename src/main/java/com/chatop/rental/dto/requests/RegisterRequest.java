package com.chatop.rental.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterRequest {
	
	@Email
    @NotEmpty
    @Size(max = 255)
    private final String email;
    
    @Size(min = 8)
    @NotEmpty
    private final String password;
    
    @Size(max = 255)
    @NotBlank
    private final String name;

    public RegisterRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}