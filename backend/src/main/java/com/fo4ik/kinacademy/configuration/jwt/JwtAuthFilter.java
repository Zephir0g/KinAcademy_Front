package com.fo4ik.kinacademy.configuration.jwt;

import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.entity.data.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // JWT service for token operations
    private final UserDetailsService userDetailsService; // UserDetailsService to load user details
    private final UserService userService;

    // This method is called for each incoming HTTP request.
    @Override
    protected void doFilterInternal(
             HttpServletRequest request,
             HttpServletResponse response,
             FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getServletPath().contains("/api/v1/components")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getServletPath().contains("/api/v1/course/search")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization"); // Get the "Authorization" header
        final String bearer = "Bearer ";
        final String jwt;
        final String username;

        // Check if the "Authorization" header is missing or doesn't start with the token prefix.
        if (authHeader == null || !authHeader.startsWith(bearer)) {
            response.sendError(401,"No Authorization header");
            return;
        }

        // Extract the JWT token from the header.
        jwt = authHeader.replace(bearer, "");
        // Extract the username from the JWT token.
        try{
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            response.sendError(401, "Token is not valid");
            return;
        }

        Response isUserActive = userService.isUserActive(username);

        if (!isUserActive.isSuccess()) {
            response.sendError(isUserActive.getHttpStatus().value(), isUserActive.getMessage());
            return;
        }

        // Check if the user is not already authenticated and the token is valid.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the UserDetailsService using the extracted username.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // If the token is valid, create an authentication token and set it in the security context.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Set additional details about the authentication request.
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set the authentication token in the security context.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // If the token is not valid, clear the security context.
                SecurityContextHolder.clearContext();
                response.sendError(401, "Token is not valid");
                return;
            }
        } else {
            // If the username is null or the token is invalid, clear the security context.
            SecurityContextHolder.clearContext();
            response.sendError(401, "Token is not valid");
            return;
        }

        // Continue processing the request.
        filterChain.doFilter(request, response);
    }
}
