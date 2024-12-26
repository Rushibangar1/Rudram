package com.app.config;

import com.app.service.UserService;
import com.app.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;

@EnableWebSecurity
public class SecurityConfiguration {


    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil JwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable()).
                authorizeHttpRequests(request -> request
                        .requestMatchers(new AntPathRequestMatcher("/api/user/SendOtp"),
                                new AntPathRequestMatcher("/api/user/VerifyOtp"))
                        .permitAll()
                        .anyRequest().authenticated()).
                httpBasic(Customizer.withDefaults()).
                sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
    }

}
