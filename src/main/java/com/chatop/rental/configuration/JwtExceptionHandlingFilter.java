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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.util.AntPathMatcher;


import com.chatop.rental.controller.advice.JwtAuthenticationException;
import com.chatop.rental.service.interfaces.JwtService;
import com.chatop.rental.service.interfaces.UserService;

@Component
@RequiredArgsConstructor
public class JwtExceptionHandlingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final HandlerExceptionResolver resolver;
    private static final Logger logger = LoggerFactory.getLogger(JwtExceptionHandlingFilter.class);
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final Set<String> PUBLIC_URLS = Set.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/uploads/**"
        );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
	        try {
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
    private boolean isPublicUrl(String requestUri) {
        return PUBLIC_URLS.stream().anyMatch(uri -> pathMatcher.match(uri, requestUri));
    }
    private void authenticateRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        validateAuthorizationHeader(authHeader);

        String jwt = authHeader.substring(7);
        jwtService.validateToken(jwt);
        setupSecurityContext(jwt, request);
    }
    private void validateAuthorizationHeader(String authHeader) {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new JwtAuthenticationException("Invalid JWT token");
        }
    }
    private void setupSecurityContext(String jwt, HttpServletRequest request) {
        String username = jwtService.getUsernameFromToken(jwt);
        if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
}
