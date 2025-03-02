package com.Zephir0g.kinacademy.configuration;

import com.Zephir0g.kinacademy.configuration.jwt.JwtAuthFilter;
import com.Zephir0g.kinacademy.entity.data.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
    // Filter responsible for JWT authentication
    private final JwtAuthFilter jwtAuthfilter;
    private final UserService userService;

    // Authentication provider for handling authentication logic
    private final AuthenticationProvider authenticationProvider;

    // Configures security filters for HTTP requests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disables CSRF protection
                .addFilterBefore(jwtAuthfilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider); // Sets the authentication provider;
        return http.build(); // Builds and returns the configured security filter chain
    }
}
