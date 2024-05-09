package com.chatop.rental.service.interfaces;

import com.chatop.rental.dto.requests.LoginRequest;
import com.chatop.rental.dto.requests.RegisterRequest;
import com.chatop.rental.dto.responses.TokenResponse;

public interface AuthService {
   TokenResponse authenticateAndGenerateToken(LoginRequest loginRequest);
   TokenResponse registerAndGenerateToken(RegisterRequest registerRequest);
}
