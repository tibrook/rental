package com.chatop.rental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LoginRequest {
		
		@Email
	    @NotEmpty
	    private String login;
	    
	    @NotEmpty
	    @Size(min = 8)
	    private String password;
	    
	    public String getLogin() {
	        return login;
	    }

	    public String getPassword() {
	        return password;
	    }
}

