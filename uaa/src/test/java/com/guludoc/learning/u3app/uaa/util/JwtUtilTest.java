package com.guludoc.learning.u3app.uaa.util;

import com.guludoc.learning.u3app.uaa.domain.Role;
import com.guludoc.learning.u3app.uaa.domain.User;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
public class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil();

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

        var token = jwtUtil.createJwtToken(user);
        log.info("Created token, {}", token);

        var parsedClaims = Jwts.parserBuilder().setSigningKey(JwtUtil.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, parsedClaims.getSubject());
    }
}
