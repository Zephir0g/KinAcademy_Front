package com.fo4ik.kinacademy.configuration.filter;

import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.entity.data.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserActivityFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getServletPath().contains("/api/v1/components")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get parameter username from request
        String username = request.getParameter("username");

        // Check user activity based on username
        Response isUserActive = userService.isUserActive(username);

        if (!isUserActive.isSuccess()) {
            response.sendError(isUserActive.getHttpStatus().value(), isUserActive.getMessage());
            return;
        }

        filterChain.doFilter(request, response);

    }


}
