package com.guludoc.learning.u3app.uaa.config;

import com.guludoc.learning.u3app.uaa.repository.LdapUserRepository;
import com.guludoc.learning.u3app.uaa.security.ldap.LdapMultiAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.util.Map;

/**
 * SpringSecurity - WebSecurityConfigurerAdapter 过时问题
 * https://blog.csdn.net/qiaohao0206/article/details/125571568
 *
 * Spring Security without the WebSecurityConfigurerAdapter
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 *
 * Adding multiple Spring Security configurations based on pathMatcher
 * https://stackoverflow.com/questions/66883174/adding-multiple-spring-security-configurations-based-on-pathmatcher
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final UserDetailsPasswordService userDetailsPasswordService;

    private final LdapUserRepository ldapUserRepository;

    /**
     * Configure HttpSecurity for MVC application
     * @param http
     * @return
     * @throws Exception
     */
    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .mvcMatchers("/login", "/error").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .clearAuthentication(true)
                .and()
                .csrf()
                .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                .and()
                .rememberMe()
                .tokenValiditySeconds(30*24*3600)
                .and()
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager(http));

        return http.build();
    }

    /**
     * Configure WebSecurity
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers( "/public/**", "/webjars/**", "/h2-console/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * Password encoder
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 默认编码的id
        String defaultEncode = "BCRYPT";

        Map<String, PasswordEncoder> encoders = Map.of(
                defaultEncode, new BCryptPasswordEncoder(),
                "SHA-1", new MessageDigestPasswordEncoder("SHA-1")
        );

        return new DelegatingPasswordEncoder(defaultEncode, encoders);
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsPasswordService(userDetailsPasswordService);

        return daoAuthenticationProvider;
    }

    @Bean
    LdapMultiAuthenticationProvider ldapMultiAuthenticationProvider() {
        return new LdapMultiAuthenticationProvider(ldapUserRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(daoAuthenticationProvider());
        builder.authenticationProvider(ldapMultiAuthenticationProvider());

        return builder.build();
    }
}
