package com.guludoc.learning.u3app.uaa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guludoc.learning.u3app.uaa.security.filter.RestAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

/**
 * SpringSecurity - WebSecurityConfigurerAdapter 过时问题
 * https://blog.csdn.net/qiaohao0206/article/details/125571568
 *
 * Spring Security without the WebSecurityConfigurerAdapter
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    private final SecurityProblemSupport securityProblemSupport;

    /**
     * Configure HttpSecurity
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RestAuthenticationFilter filter) throws Exception {

        http.authorizeRequests(
                authz -> authz.mvcMatchers("/login", "/error",  "/authorize/**").permitAll()
                        .mvcMatchers("/admin/**").hasRole("ADMIN")
                        .mvcMatchers("/api/**").hasRole("USER")
                        .anyRequest().denyAll()
        );

        http.formLogin(form -> {
            form.loginPage("/login");
//                    .successHandler(this::authenticationSuccessHandler)
//                    .failureHandler(this::authenticationFailureHandler);
        });

        // CSRF Attack
        http.csrf(csrf -> {
            csrf.ignoringAntMatchers("/authorize/**", "/api/**");
            csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository());
        });

        // Remember me
        http.rememberMe(rememberMe -> {
            rememberMe.tokenValiditySeconds(30*24*3600);
        });

        // Logout
        http.logout( logout -> {
            logout.logoutUrl("/perform_logout");
            logout.clearAuthentication(true);
        });

        http.exceptionHandling(exp -> {
            exp.authenticationEntryPoint(securityProblemSupport)
                    .accessDeniedHandler(securityProblemSupport);
        });

        // customize filter
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RestAuthenticationFilter restAuthenticationFilter(AuthenticationManager authenticationManager) {
        RestAuthenticationFilter filter = new RestAuthenticationFilter(objectMapper);

        filter.setAuthenticationSuccessHandler(this::authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(this::authenticationFailureHandler);

        filter.setAuthenticationManager(authenticationManager);

        filter.setFilterProcessesUrl("/authorize/login");

        return filter;
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
}
