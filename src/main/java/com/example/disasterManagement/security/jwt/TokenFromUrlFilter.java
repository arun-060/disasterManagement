package com.example.disasterManagement.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

public class TokenFromUrlFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenFromUrlFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String token = request.getParameter("token");
            
            if (StringUtils.hasText(token)) {
                logger.debug("Found token in URL parameter: {}", token.substring(0, Math.min(10, token.length())) + "...");
                
                // Log the current request details for debugging
                logger.debug("Request URI: {}", request.getRequestURI());
                logger.debug("Request Method: {}", request.getMethod());
                
                // Wrap the request to add the token to the Authorization header
                request = new AuthorizationHeaderRequestWrapper(request, "Bearer " + token);
                
                // Log that we've added the Authorization header
                logger.debug("Added Authorization header with Bearer token");
            } else {
                // Check if token is in cookies
                jakarta.servlet.http.Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (jakarta.servlet.http.Cookie cookie : cookies) {
                        if ("jwtToken".equals(cookie.getName()) && StringUtils.hasText(cookie.getValue())) {
                            logger.debug("Found token in cookie");
                            request = new AuthorizationHeaderRequestWrapper(request, "Bearer " + cookie.getValue());
                            logger.debug("Added Authorization header with Bearer token from cookie");
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot extract token from URL parameter: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
    
    // Custom request wrapper to add Authorization header
    private static class AuthorizationHeaderRequestWrapper extends jakarta.servlet.http.HttpServletRequestWrapper {
        private final String authorizationHeader;
        
        public AuthorizationHeaderRequestWrapper(HttpServletRequest request, String authorizationHeader) {
            super(request);
            this.authorizationHeader = authorizationHeader;
        }
        
        @Override
        public String getHeader(String name) {
            if ("Authorization".equalsIgnoreCase(name) && authorizationHeader != null) {
                return authorizationHeader;
            }
            return super.getHeader(name);
        }
        
        @Override
        public Enumeration<String> getHeaders(String name) {
            if ("Authorization".equalsIgnoreCase(name) && authorizationHeader != null) {
                return java.util.Collections.enumeration(java.util.Collections.singletonList(authorizationHeader));
            }
            return super.getHeaders(name);
        }
        
        @Override
        public Enumeration<String> getHeaderNames() {
            // Get the original header names
            Enumeration<String> originalHeaderNames = super.getHeaderNames();
            
            // Convert to a list for manipulation
            java.util.List<String> headerNames = new java.util.ArrayList<>();
            while (originalHeaderNames.hasMoreElements()) {
                headerNames.add(originalHeaderNames.nextElement());
            }
            
            // Add Authorization if it's not already there
            if (!headerNames.contains("Authorization") && authorizationHeader != null) {
                headerNames.add("Authorization");
            }
            
            // Return as an enumeration
            return java.util.Collections.enumeration(headerNames);
        }
    }
} 