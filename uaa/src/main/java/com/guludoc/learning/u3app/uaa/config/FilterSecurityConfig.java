package com.guludoc.learning.u3app.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SpringSecurity - WebSecurityConfigurerAdapter 过时问题
 * https://blog.csdn.net/qiaohao0206/article/details/125571568
 */
@Configuration
@EnableWebSecurity(debug = true)
public class FilterSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests(req -> req.antMatchers("/api/**").authenticated())
                .formLogin(form -> form.loginPage("/login"));

        return http.build();
    }
}
