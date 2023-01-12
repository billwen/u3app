package com.guludoc.learning.u3app.uaa.service;

import com.guludoc.learning.u3app.uaa.domain.JwtTokens;
import com.guludoc.learning.u3app.uaa.repository.UserRepository;
import com.guludoc.learning.u3app.uaa.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public JwtTokens login(String username, String password) throws AuthenticationException {
        return userRepository.findUserByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    String accessToken = jwtUtil.createAccessToken(user);
                    String refreshToken = jwtUtil.createRefreshToken(user);
                    return new JwtTokens(accessToken, refreshToken);
                })
                .orElseThrow( () -> new BadCredentialsException("Username or password failed"));
    }

    public JwtTokens refresh(String accessToken, String refreshToken) throws AuthenticationException {
        if (jwtUtil.validateRefreshToken(refreshToken) && jwtUtil.validateAccessTokenWithoutExpiration(accessToken)) {
            return new JwtTokens(jwtUtil.createAccessTokenWithRefreshToken(refreshToken), refreshToken);
        }

        throw new AccessDeniedException("Access denied");
    }
}
