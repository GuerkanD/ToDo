package com.service.todo_backend.security;

import com.service.todo_backend.service.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AuthenticationFilter implements Filter {

    private final JwtService jwtService;

    AuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = extractToken(httpRequest);
        try {
            if (Arrays.stream(WebSecurityConfig.getNonAuthenticatedPaths()).toList().contains(httpRequest.getRequestURI())) {
                chain.doFilter(request, response);
                return;
            }
            if (token != null && jwtService.validateToken(token)) {
                String userId = jwtService.getUserIdFromToken(token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,null,null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } else {
                logger.info("Invalid token");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception e) {
            logger.error("Error while validating token: {}", e.getMessage());
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
