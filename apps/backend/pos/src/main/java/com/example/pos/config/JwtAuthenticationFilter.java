package com.example.pos.config;

import com.example.pos.dto.SecurityUser;
import com.example.pos.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String authorizationHeader = request.getHeader("Authorization");

            String username = null;
            String jwt = null;

            // Extract JWT token from Authorization header
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                try {
                    username = jwtTokenUtil.extractUsername(jwt);
                } catch (Exception e) {
                    logger.warn("Invalid JWT token format: {}", e.getMessage());
                }
            }

            // Set up authentication if we have a valid username and no existing authentication
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                    // Validate token expiration
                    if (jwtTokenUtil.isTokenExpired(jwt)) {
                        logger.warn("JWT token is expired for user: {}", username);
                        SecurityContextHolder.clearContext();
                    } else {
                        // Create authentication token and set it in security context
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("Authentication set for user: {}", username);
                    }
                } catch (Exception e) {
                    logger.warn("Failed to load user details for username: {}", username, e);
                    SecurityContextHolder.clearContext();
                }
            } else if (username == null && authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // Clear context if we have a Bearer token but couldn't extract username
                logger.warn("Could not extract username from JWT token");
                SecurityContextHolder.clearContext();
            }

        } catch (Exception e) {
            logger.error("Error processing JWT authentication", e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
} 