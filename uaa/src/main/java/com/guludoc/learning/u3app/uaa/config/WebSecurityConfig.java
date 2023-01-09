package com.guludoc.learning.u3app.uaa.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
public class WebSecurityConfig {

    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/error", "/login", "/public/**", "/webjars/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
