package com.chatop.rental.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.util.AntPathMatcher;

import com.chatop.rental.exception.JwtAuthenticationException;
import com.chatop.rental.service.interfaces.JwtService;
import com.chatop.rental.service.interfaces.UserService;

@Component
@RequiredArgsConstructor
public class JwtExceptionHandlingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final HandlerExceptionResolver resolver;
    private static final Logger logger = LoggerFactory.getLogger(JwtExceptionHandlingFilter.class);
    private final AntPathMatcher pathMatcher = new AntPathMatcher(); // Utility for URL pattern matching.
    private static final Set<String> PUBLIC_URLS = Set.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/uploads/**"
        );
    /**
     * Core method to perform JWT checking and authentication on each request, except for the public URLs.
     * @param request The incoming HTTP request.
     * @param response The HTTP response object.
     * @param filterChain The filter chain to pass control to the next filter or resource.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs during processing.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
	        try {
	            // Check if the request URI is one of the public URLs that don't require authentication.
	            if (isPublicUrl(request.getRequestURI())) {
	                filterChain.doFilter(request, response);
	                return;
	            }
	            authenticateRequest(request);
	        } catch (JwtAuthenticationException e) {
	            logger.error("JWT Authentication error: {}", e.getMessage());
	            resolver.resolveException(request, response, null, e);
	            return;
	        }
	        filterChain.doFilter(request, response);
    }
    /**
     * Helper method to determine if a request URI matches any of the public URL patterns.
     * @param requestUri The URI of the incoming request.
     * @return true if the URI matches any public URL patterns, false otherwise.
     */
    private boolean isPublicUrl(String requestUri) {
        return PUBLIC_URLS.stream().anyMatch(uri -> pathMatcher.match(uri, requestUri));
    }
    /**
     * Validates and authenticates the JWT from the request header.
     * @param request The incoming HTTP request containing the Authorization header.
     */
    private void authenticateRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        validateAuthorizationHeader(authHeader);

        String jwt = authHeader.substring(7);
        jwtService.validateToken(jwt);
        setupSecurityContext(jwt, request);
    }
    /**
     * Validates the format of the Authorization header.
     * @param authHeader The Authorization header content.
     * @throws JwtAuthenticationException if the header is invalid.
     */
    private void validateAuthorizationHeader(String authHeader) {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new JwtAuthenticationException("Invalid JWT token");
        }
    }
    /**
     * Sets up the security context for the current request if the JWT is valid.
     * @param jwt The JWT extracted from the Authorization header.
     * @param request The current HTTP request.
     */
    private void setupSecurityContext(String jwt, HttpServletRequest request) {
	  try {
	        String username = jwtService.getUsernameFromToken(jwt);
	        if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
	            UserDetails userDetails;
	            try {
	                userDetails = userService.loadUserByUsername(username);
	            } catch (UsernameNotFoundException e) {
	                throw new JwtAuthenticationException("User does not exist.");
	            }

	            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                userDetails, null, userDetails.getAuthorities());
	            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	        }
	    } catch (JwtAuthenticationException e) {
	        throw e;  // Re-throw the exception to be caught in doFilterInternal
	    }
    }
}
