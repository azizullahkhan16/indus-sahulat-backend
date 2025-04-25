package com.aktic.indussahulatbackend.filter;

import com.aktic.indussahulatbackend.constant.Constants;
import com.aktic.indussahulatbackend.security.CustomUserDetailsService;
import com.aktic.indussahulatbackend.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Qualifier("userDetailsServiceImpl")
    @Lazy
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Bypass filter for specific paths
        for (String path : Constants.FILTER_BYPASS_PATHS) {
            if (request.getServletPath().contains(path)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // Process /api and /chat paths
        boolean isApiPath = request.getServletPath().startsWith("/api");
        boolean isWebSocketPath = request.getServletPath().startsWith("/chat");

        if (!isApiPath && !isWebSocketPath) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT from header or query parameter
        String jwt = resolveToken(request);
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String userPhone = jwtService.extractUsername(jwt);
        String userRole = jwtService.extractRole(jwt);

        if (userPhone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsernameAndRole(userPhone, userRole);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // Check Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        // Check query parameter for WebSocket
        String token = request.getParameter("token");
        if (token != null && !token.isEmpty()) {
            return token;
        }
        return null;
    }
}
