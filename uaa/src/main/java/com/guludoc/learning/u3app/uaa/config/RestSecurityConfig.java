package com.guludoc.learning.u3app.uaa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guludoc.learning.u3app.uaa.security.filter.JwtFilter;
import com.guludoc.learning.u3app.uaa.security.filter.RestAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Slf4j
@RequiredArgsConstructor
@Configuration
@Import(SecurityProblemSupport.class)
public class RestSecurityConfig {

    private final AppProperties appProperties;

    private final ObjectMapper objectMapper;

    private final SecurityProblemSupport securityProblemSupport;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final Environment environment;

    /**
     * Setup security filter chain
     * Samples
     * mvcMatchers("/api/users/{username}/**")
     * access("hasRole('ADMIN') or authentication.name.equals(#username)")
     *
     * public class UserValidationService {
     *     public boolean checkUsername(Authentication authentication, String username) {
     *         return authentication.getName().equals(username);
     *     }
     * }
     *
     * .mvcMatchers("/api/users/{username}/**")
     * .access("@userValidationService.checkUsername(authentication, #username)")
     * @param http
     * @return
     * @throws Exception
     */
    @Order(1)
    @Bean
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http) throws Exception {

        var filter = restAuthenticationFilter();

        // Only works for /api/** and /authorize/**
        http.requestMatchers()
                .mvcMatchers("/api/**", "/authorize/**")
                .and()
                .authorizeRequests()
                .mvcMatchers("/authorize/**").permitAll()
                .mvcMatchers("/api/users/manager").hasRole("MANAGER")
                .mvcMatchers("/api/users/**").access("hasRole('ADMIN') or authentication.name.equals(#username)")
                .anyRequest().hasRole("USER")
                .and()
                .httpBasic()
                .and()
                .formLogin().disable()
                .csrf().disable()
                .rememberMe().disable()
                .exceptionHandling()
                .authenticationEntryPoint(securityProblemSupport)
                .accessDeniedHandler(securityProblemSupport)
                .and()
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager);

        return http.build();
    }

    @Bean
    public RestAuthenticationFilter restAuthenticationFilter() {
        RestAuthenticationFilter filter = new RestAuthenticationFilter(objectMapper);

        filter.setAuthenticationSuccessHandler(this::authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(this::authenticationFailureHandler);

        filter.setAuthenticationManager(authenticationManager);

        filter.setFilterProcessesUrl("/authorize/login");

        return filter;
    }

    @Bean
    public JwtFilter jwtFilter() {
        JwtFilter jwtFilter = new JwtFilter(appProperties);

        return jwtFilter;
    }

    /**
     * Authentication Success handler
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    private void authenticationSuccessHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ObjectMapper om = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().println(om.writeValueAsString(authentication));
        log.debug("认证成功");
    }

    /**
     * Authentication failure handler
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    private void authenticationFailureHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ObjectMapper om = new ObjectMapper();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        var errs = Map.of("title", "认证失败", "details", exception.getLocalizedMessage());

        response.getWriter().println(om.writeValueAsString(errs));
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

    /**
     * 可以在Spring Security 中配置cors
     */
    // @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允许跨域访问的主机
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        } else {
            configuration.setAllowedOrigins(Collections.singletonList("https://uaa.imooc.com"));
        }

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addExposedHeader("X-Authenticate");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
