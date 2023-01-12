package com.guludoc.learning.u3app.uaa.util;

import com.guludoc.learning.u3app.uaa.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.AccessControlException;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
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

    public boolean validateAccessTokenWithoutExpiration(String accessToken) {
        return validateToken(accessToken, ACCESS_KEY, true);
    }
    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, ACCESS_KEY,  false);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, REFRESH_KEY, false);
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

    private boolean validateToken(String token, Key key, boolean isExpiredInvalid) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parse(token);
        }
        catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            if ( e instanceof ExpiredJwtException) {
                return !isExpiredInvalid;
            }

            return false;
        }

        return true;
    }

    public String createAccessTokenWithRefreshToken(String refreshToken) throws AuthenticationException {
        return parseClaims(refreshToken, REFRESH_KEY)
                .map(claims -> Jwts.builder()
                        .setClaims(claims)
                        .setExpiration(new Date(System.currentTimeMillis() + appProperties.getJwt().getAccessTokenExpireTime()))
                        .setIssuedAt(new Date())
                        .signWith(ACCESS_KEY, SignatureAlgorithm.HS512)
                        .compact()
                )
                .orElseThrow( () -> new AccessControlException("Access denied "));
    }

    private Optional<Claims> parseClaims(String token, Key key) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Optional.of(claims);
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
