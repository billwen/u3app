package com.guludoc.learning.u3app.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig  {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf( csrf -> {
            csrf.disable();
        });

        http.httpBasic(basic -> {
            basic.realmName("MyName");
        });

//        http.formLogin(Customizer.withDefaults())
//                .authorizeRequests()
//                .mvcMatchers("/api/greeting").authenticated();

        http.authorizeRequests()
                .mvcMatchers("/api/greeting").authenticated();
        return http.build();
    }
}
