package com.guludoc.learning.u3app.uaa.util;

import com.guludoc.learning.u3app.uaa.config.AppProperties;
import com.guludoc.learning.u3app.uaa.domain.Role;
import com.guludoc.learning.u3app.uaa.domain.User;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
public class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil(new AppProperties());

    @Test
    public void givenUserDetails_thenCreateTokenSuccess() {
        String username = "user";
        var authorities = Set.of(
                Role.builder().authority("ROLE_USER").build(),
                Role.builder().authority("ROLE_ADMIN").build()
        );

        var user = User.builder()
                .username(username)
                .authorities(authorities)
                .build();

        var appProperties = new AppProperties();

        var token = jwtUtil.createJwtToken(user, JwtUtil.ACCESS_KEY, appProperties.getJwt().getAccessTokenExpireTime());
        log.info("Created token, {}", token);

        var parsedClaims = Jwts.parserBuilder().setSigningKey(JwtUtil.ACCESS_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, parsedClaims.getSubject());
    }
}
