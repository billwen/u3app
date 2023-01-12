package com.guludoc.learning.u3app.uaa.util;

import com.guludoc.learning.u3app.uaa.config.AppProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final AppProperties appProperties;

    // 用于签名的访问令牌的密码
    public static final Key ACCESS_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // 用于签名刷新令牌的密钥
    public static final Key REFRESH_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String createAccessToken(UserDetails userDetails) {
        return createJwtToken(userDetails, ACCESS_KEY, appProperties.getJwt().getAccessTokenExpireTime());
    }

    public String createRefreshToken(UserDetails userDetails) {
        return createJwtToken(userDetails, REFRESH_KEY, appProperties.getJwt().getRefreshTokenExpireTime());
    }

    public String createJwtToken(UserDetails userDetails, Key key, long timeToExpire) {
        long current = System.currentTimeMillis();

        return Jwts.builder()
                .setId("mooc")
                .claim("authorities", userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(current))
                .setExpiration(new Date(current + timeToExpire))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
