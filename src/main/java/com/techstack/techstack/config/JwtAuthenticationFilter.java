package com.techstack.techstack.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // Define the list of public paths that the filter should NOT process
    private final List<AntPathRequestMatcher> excludedPaths = Arrays.asList(
            new AntPathRequestMatcher("/api/auth/**"),
            new AntPathRequestMatcher("/api/products/**")
    );


    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return excludedPaths.stream().anyMatch(p -> p.matches(request));
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("Processing JWT filter for secured request: " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If no token is present, continue to the next filter in the chain.
            // If the endpoint is secured, Spring Security will deny access.
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // Extract token from "Bearer <token>"
        try {
            userEmail = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            System.err.println("JWT token is invalid: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }


        // If the token is valid and there's no authentication in the context, set it up.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt, userEmail)) {
                // In a real application, you would load UserDetails here
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        null // No authorities/roles in this example
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication successful for user: " + userEmail);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}