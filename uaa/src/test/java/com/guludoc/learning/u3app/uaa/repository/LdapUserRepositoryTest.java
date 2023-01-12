package com.guludoc.learning.u3app.uaa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.ldap.DataLdapTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
@DataLdapTest
public class LdapUserRepositoryTest {

    @Autowired
    private LdapUserRepository userRepository;

    @Test
    public void givenUsernameAndPassword_ThenFindUserSuccess() {
        String username = "zhaoliu";
        String password = "123";
        var user = userRepository.findByUsernameAndPassword(username, password);
        assertTrue(user.isPresent());
    }

    @Test
    public void givenUsernameAndPassword_ThenFindUserFailed() {
        String username = "zhaoliu";
        String password = "123456";
        var user = userRepository.findByUsernameAndPassword(username, password);
        assertFalse(user.isPresent());
    }
}
