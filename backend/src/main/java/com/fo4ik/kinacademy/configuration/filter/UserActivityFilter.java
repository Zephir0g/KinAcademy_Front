package com.fo4ik.kinacademy.configuration.filter;

import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.entity.data.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
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
        if (request.getServletPath().contains("/api/v1/course")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get parameter username from request
        String username = request.getParameter("username");

        // Check user activity based on username
        Response isUserActive = userService.isUserActive(username);


        if(!isUserActive.isSuccess()) {
            response.sendError(isUserActive.getHttpStatus().value(), isUserActive.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    /*@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getServletPath().contains("/api/v1/auth")) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getServletPath().contains("/api/v1/components")) {
            chain.doFilter(request, response);
            return;
        }

        // Get parameter username from request
        String username = httpRequest.getParameter("username");

        // Check user activity based on username
        Response isUserActive = userService.isUserActive(username);


        if(!isUserActive.isSuccess()) {
            httpResponse.sendError(isUserActive.getHttpStatus().value(), isUserActive.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }*/
}
