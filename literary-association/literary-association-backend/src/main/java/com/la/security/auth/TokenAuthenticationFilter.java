package com.la.security.auth;

import com.la.security.TokenUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private TokenUtils tokenUtils;

    private UserDetailsService userDetailsService;

    public TokenAuthenticationFilter(TokenUtils tokenUtils, UserDetailsService userDetailsService) {
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String username;
        String authenticationToken = tokenUtils.getToken(httpServletRequest);

        if (authenticationToken != null) {
            // uzmi username iz tokena
            username = tokenUtils.getUsernameFromToken(authenticationToken);

            if (username != null) {
                // uzmi username iz tokena
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // uzmi username iz tokena
                if (tokenUtils.validateToken(authenticationToken, userDetails)) {
                    // uzmi username iz tokena
                    TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                    authentication.setToken(authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // prosledi request dalje u sledeci filter
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
