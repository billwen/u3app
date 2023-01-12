package com.guludoc.learning.u3app.uaa.security.filter;

import com.guludoc.learning.u3app.uaa.config.AppProperties;
import com.guludoc.learning.u3app.uaa.util.JwtUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AppProperties appProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkJwtToken(request)) {
            // Authorization: Bearer xxxx
            validateToken(request)
                    .filter(claims -> claims.get("authorities") != null)
                    .ifPresentOrElse(this::setupSpringAuthentication, SecurityContextHolder::clearContext);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 检查jwt token是否在http 报头中
     * @param request http 请求
     * @return 是否有jwt token
     */
    private boolean checkJwtToken(HttpServletRequest request) {
        String header = request.getHeader(appProperties.getJwt().getHeader());

        return header != null && header.startsWith(appProperties.getJwt().getPrefix());
    }

    private Optional<Claims> validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(appProperties.getJwt().getHeader())
                .replace(appProperties.getJwt().getPrefix(), "");

        try {
            return Optional.of(Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.ACCESS_KEY)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody());
        }
        catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private void setupSpringAuthentication(Claims claims) {
        Optional.of(claims.get("authorities"))
                .map(auths -> {
                    var rawList = convertObjectToList(auths);
                    List<GrantedAuthority> authorities = rawList.stream()
                            .map(String::valueOf)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
                })
                .ifPresentOrElse( token ->
                    SecurityContextHolder.getContext().setAuthentication(token),
                    SecurityContextHolder::clearContext
                );
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }

        return list;
    }
}
