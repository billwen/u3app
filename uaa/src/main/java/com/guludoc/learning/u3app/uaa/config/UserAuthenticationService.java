package com.guludoc.learning.u3app.uaa.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {

    public boolean isValidUser(Authentication authentication, String username) {
        return authentication.getName().equals(username);
    }
}
