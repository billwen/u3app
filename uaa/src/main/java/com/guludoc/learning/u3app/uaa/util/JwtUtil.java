package com.guludoc.learning.u3app.uaa.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // 用于签名
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String createJwtToken(UserDetails userDetails) {
        long current = System.currentTimeMillis();

        return Jwts.builder()
                .setId("mooc")
                .claim("authorities", userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(current))
                .setExpiration(new Date(current + 60_000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
