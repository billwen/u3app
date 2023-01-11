package com.guludoc.learning.u3app.uaa.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Slf4j
public class UserTest {

    @Test
    public void generateEncodedPassword() {
        DelegatingPasswordEncoder encoder = passwordEncoder();

        String userPass = encoder.encode("123");
        log.info("User Pass {}", userPass);

        MessageDigestPasswordEncoder digest = new MessageDigestPasswordEncoder("SHA-1");
        String oldUserPass = digest.encode("123");
        log.info("Old user pass {}", oldUserPass);
    }

    private DelegatingPasswordEncoder passwordEncoder() {
        // 默认编码的id
        String defaultEncode = "BCRYPT";

        Map<String, PasswordEncoder> encoders = Map.of(
                defaultEncode, new BCryptPasswordEncoder(),
                "SHA-1", new MessageDigestPasswordEncoder("SHA-1")
        );

        return new DelegatingPasswordEncoder(defaultEncode, encoders);
    }
}
