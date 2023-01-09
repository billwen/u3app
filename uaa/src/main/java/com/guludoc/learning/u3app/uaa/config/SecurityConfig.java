package com.guludoc.learning.u3app.uaa.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.util.Collection;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

/**
 * SpringSecurity - WebSecurityConfigurerAdapter 过时问题
 * https://blog.csdn.net/qiaohao0206/article/details/125571568
 *
 * Spring Security without the WebSecurityConfigurerAdapter
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapte
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    /**
     * Configure HttpSecurity
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                        .mvcMatchers("/login", "/error")
                                .permitAll();

        http.authorizeRequests(
                authz -> authz.mvcMatchers("/login", "/error").permitAll()
                        .anyRequest().authenticated());

        http.formLogin(form -> form.loginPage("/login"));

        // CSRF Attack
        http.csrf(csrf -> {
            csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository());
        });

        // Remember me
        http.rememberMe(rememberMe -> {
            rememberMe.tokenRepository(new JdbcTokenRepositoryImpl())
                    .tokenValiditySeconds(30*24*3600);
        });

        // Logout
        http.logout( logout -> {
            logout.logoutUrl("/perform_logout");
            logout.clearAuthentication(true);
        });

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * In-Memory Authentication
     * @return
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        PasswordEncoder encoder = this.passwordEncoder();

        UserDetails user = User.withUsername("user")
                .password(encoder.encode("123"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    /**
     * 在响应中设置Cookie的SameSite属性，用于预防CSRF攻击
     * @return
     */
    private AuthenticationSuccessHandler jsonLoginSuccessHandler() {
        return (req, res, auth) -> {
            Collection<String> headers = res.getHeaders(SET_COOKIE);
            res.addHeader(SET_COOKIE, String.format("%s; %s", "header", "SameSite=Strict"));
        };
    }
}
