package com.guludoc.learning.u3app.uaa.service;

import com.guludoc.learning.u3app.uaa.config.Constants;
import com.guludoc.learning.u3app.uaa.domain.JwtTokens;
import com.guludoc.learning.u3app.uaa.domain.User;
import com.guludoc.learning.u3app.uaa.repository.RoleRepository;
import com.guludoc.learning.u3app.uaa.repository.UserRepository;
import com.guludoc.learning.u3app.uaa.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

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

    public boolean isUsernameExisted(String username) {
        return userRepository.countByUsername(username) > 0;
    }

    public boolean isEmailExisted(String email) {
        return userRepository.countByEmail(email) > 0;
    }

    public boolean isMobileExisted(String mobile) {
        return userRepository.countByMobile(mobile) > 0;
    }

    @Transactional
    public User register(User user) throws AuthenticationException {
        return roleRepository.findByAuthority(Constants.ROLE_USER)
                .map(role -> {
                    var userToSave = user.withAuthorities(Set.of(role))
                            .withPassword(passwordEncoder.encode(user.getPassword()))
                            .withAccountNonExpired(true)
                            .withAccountNonLocked(true)
                            .withEnabled(true);

                    return userRepository.save(userToSave);
                })
                .orElseThrow( () -> new UsernameNotFoundException("Can't find user"));
    }
}
