
package com.skillswap.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Reads the Authorization: Bearer <jwt> header
 * and adds the user ID to the security context.
 */
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwt;

    public JwtFilter(JwtService jwt) {
        this.jwt = jwt;
    }

    // Skip browser CORS preflight requests.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        return "OPTIONS".equalsIgnoreCase(req.getMethod());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {

        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                Long id = jwt.parse(header.substring(7));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                id, null, List.of());

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

            } catch (JwtException | IllegalArgumentException e) {
                // Invalid token: request remains unauthenticated.
            }
        }

        chain.doFilter(req, res);
    }
}
