package com.app.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.entity.UserInfo;
import com.app.service.UserService;
import com.app.util.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve the Authorization header
        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String phoneNumber = null;

        // Extract the token if the header starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            phoneNumber = jwtUtil.extractUsername(token); // Assuming phoneNumber is used as the username
        }

        // Proceed only if the phone number is found and the user is not already authenticated
        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user information based on the phone number
            UserInfo userInfo = (UserInfo) userService.loadUserByUsername(phoneNumber);

            // Validate the token against the user information
            if (jwtUtil.validateToken(token, userInfo)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authenticated user in the security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
