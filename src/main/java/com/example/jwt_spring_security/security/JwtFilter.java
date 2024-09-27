package com.example.jwt_spring_security.security;

import com.example.jwt_spring_security.exception.JwtTokenException;
import com.example.jwt_spring_security.model.JwtAuthenticationToken;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;


import java.io.IOException;
import java.util.Objects;

@Component

public class JwtFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public JwtFilter() {
        super("/rest/**");

    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        SecurityContextHolder.getContext().setAuthentication(authResult);
//        getSuccessHandler().onAuthenticationSuccess(request, response, chain, authResult);
//        System.out.println("Hi");
        super.successfulAuthentication(request,response,chain,authResult);
        chain.doFilter(request, response); // Continue the filter chain
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("Authentication failed: " + failed.getMessage());
        jwtAuthenticationEntryPoint.commence(request, response, failed);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
            String token = request.getHeader("Authorization");
            if (StringUtils.isEmpty(token)) {
                throw new JwtTokenException("NO TOKEN");
            }
            System.out.println("Token is " + token);
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
            return getAuthenticationManager().authenticate(jwtAuthenticationToken);

    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(new ProviderManager(jwtAuthenticationProvider));
    }

    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler);
    }

    @PostConstruct
    public void init() {
        setAuthenticationManager(new ProviderManager(jwtAuthenticationProvider));
        setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler); // Set the success handler
    }
}
