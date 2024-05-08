package com.chatop.rental.service.interfaces;

import org.springframework.security.core.Authentication;

public interface JwtService {
	String generateToken(Authentication authentication);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}
