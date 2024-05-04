package com.chatop.rental.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {
	
	@Value("${jwt.expiration}")
    private long jwtExpiration; 

	@Value("${JWT_SECRET}")
	private String jwtSecret;
	
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new FileSystemResource(".env"));
        return configurer;
    }

    /**
     * Configures the security filter chain that applies to incoming HTTP requests.
     * @param http Security configurations bound to the context path.
     * @return Configured SecurityFilterChain object that Spring Security uses to handle security.
     * @throws Exception when there is a configuration error.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register", "/api/auth/login","/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2
            	.jwt(Customizer.withDefaults()) 
                .authenticationEntryPoint(jwtAuthenticationEntryPoint()));
          return http.build();
    }

    /**
     * Provides a custom AuthenticationEntryPoint that is used when authentication fails.
     * This method customizes the response to use JSON format and return a 401 status code.
     * @return an instance of AuthenticationEntryPoint that handles authentication failures.
     */
    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{}");
        };
    }

    /**
     * Exposes the default AuthenticationManager as a Bean.
     * AuthenticationManager is the main strategy interface for Authentication management in Spring Security.
     * @return the AuthenticationManager bean configured by the AuthenticationConfiguration.
     * @throws Exception when an error occurs during the configuration.
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides a BCryptPasswordEncoder bean to encode passwords.
     * BCryptPasswordEncoder is a password encoder that uses the BCrypt strong hashing function.
     * @return a BCryptPasswordEncoder to use for hashing user passwords.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a JwtDecoder bean that can be used to decode JWTs in the application.
     * It uses a HMAC with SHA-256 based algorithm and a predefined secret key.
     * @return a JwtDecoder that decodes JWTs using the configured secret key and HMAC-SHA256 algorithm.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /**
     * Creates a JwtEncoder bean to encode JWTs.
     * It uses the provided secret key and HMAC-SHA256 algorithm to create signed JWTs.
     * @return a JwtEncoder to create JWTs using the configured secret key.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecret.getBytes()));
    }

   
}
