package com.fo4ik.kinacademy.configuration;

import com.fo4ik.kinacademy.configuration.jwt.JwtAuthFilter;
import com.fo4ik.kinacademy.entity.data.service.UserService;
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
    // White list of URLs that are allowed without authentication
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/components/**",
            "/v1/api-docs/**",
            "/v1/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html"};

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
                /*.authorizeHttpRequests((requests) -> {
                    // Configures authorization rules for specific URLs
                    requests.requestMatchers(WHITE_LIST_URL).permitAll()
                            // Requires authentication for any other request
                            .anyRequest().authenticated();
                });*/
                .sessionManagement(customizer -> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider); // Sets the authentication provider;
        return http.build(); // Builds and returns the configured security filter chain
    }
}
