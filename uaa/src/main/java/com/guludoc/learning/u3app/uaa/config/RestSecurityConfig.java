package com.guludoc.learning.u3app.uaa.config;

import com.guludoc.learning.u3app.uaa.security.filter.RestAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@RequiredArgsConstructor
@Configuration
@Import(SecurityProblemSupport.class)
public class RestSecurityConfig {

    private final SecurityProblemSupport securityProblemSupport;

    @Order(1)
    @Bean
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http, RestAuthenticationFilter filter) throws Exception {

        // Only works for /api/** and /authorize/**
        http.requestMatchers()
                .mvcMatchers("/api/**", "/authorize/**")
                .and()
                .authorizeRequests()
                .mvcMatchers("/authorize/**").permitAll()
                .anyRequest().hasRole("USER")
                .and()
                .formLogin().disable()
                .csrf().disable()
                .rememberMe().disable()
                .exceptionHandling()
                .authenticationEntryPoint(securityProblemSupport)
                .accessDeniedHandler(securityProblemSupport)
                .and()
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
