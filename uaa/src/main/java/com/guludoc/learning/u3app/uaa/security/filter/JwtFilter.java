package com.guludoc.learning.u3app.uaa.security.filter;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }

    private void setupSpringAuthentication(Claims claims) {
        Optional.of(claims.get("authorities"))
                .map(auths -> {
                    List<String> rawList = CollectionUtil.conv(auths);
                    List<GrantedAuthority> authorities = rawList.stream()
                            .map(String::valueOf)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
                })
                .ifPresentOrElse( token ->
                    SecurityContextHolder.getContext().setAuthentication(token),
                    SecurityContextHolder::clearContext;
                );
    }
}
