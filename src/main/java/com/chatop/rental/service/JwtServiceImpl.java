package com.chatop.rental.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.chatop.rental.exception.JwtAuthenticationException;
import com.chatop.rental.service.interfaces.JwtService;

/**
 * Implementation of JwtService interface providing JWT token generation, validation, and extraction functionalities.
 */
@Service
public class JwtServiceImpl implements JwtService{

	private JwtEncoder jwtEncoder;
	private JwtDecoder jwtDecoder;
		
	@Value("${jwt.expiration}")
	private long jwtExpiration; 
	
	/**
	 * Constructs a JwtServiceImpl instance.
	 * @param jwtEncoder JwtEncoder instance for encoding JWT tokens.
	 * @param jwtDecoder JwtDecoder instance for decoding JWT tokens.
	 */
	public JwtServiceImpl(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
		this.jwtEncoder = jwtEncoder;
		this.jwtDecoder = jwtDecoder;
	}
	/**
	 * Generates a JWT token for the provided authentication.
	 * @param authentication Authentication object representing the authenticated user.
	 * @return JWT token generated for the authentication.
	 */
	public String generateToken(Authentication authentication) {
    	Instant now = Instant.now();
 		JwtClaimsSet claims = JwtClaimsSet.builder()
          		  .issuer("self")
           		  .issuedAt(now)
           		  .expiresAt(now.plus(jwtExpiration, ChronoUnit.SECONDS))
          		  .subject(authentication.getName())
          		  .build();
		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
		return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
	}
	/**
	 * Validates the provided JWT token.
	 * @param token JWT token to be validated.
	 * @return true if the token is valid, false otherwise.
	 * @throws JwtAuthenticationException if the token is malformed or expired.
	 */
	public boolean validateToken(String token) {
	    try {
	        Jwt jwt = jwtDecoder.decode(token);
	        if (jwt.getExpiresAt().isBefore(Instant.now())) {
	            throw new JwtAuthenticationException("Expired JWT token");
	        }
	        return true;
	    } catch (Exception e) {
	        throw new JwtAuthenticationException("Malformed JWT token: " + e.getMessage());
	    }
	}
	/**
     * Extracts the username from the provided JWT token.
     * @param token JWT token from which to extract the username.
     * @return username extracted from the token.
     */
    public String getUsernameFromToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }
}
