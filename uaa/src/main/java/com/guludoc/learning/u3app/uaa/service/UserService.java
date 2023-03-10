package com.guludoc.learning.u3app.uaa.service;

import com.guludoc.learning.u3app.uaa.annotation.RoleAdminOrSelfWithUserParam;
import com.guludoc.learning.u3app.uaa.config.Constants;
import com.guludoc.learning.u3app.uaa.domain.JwtTokens;
import com.guludoc.learning.u3app.uaa.domain.User;
import com.guludoc.learning.u3app.uaa.repository.RoleRepository;
import com.guludoc.learning.u3app.uaa.repository.UserRepository;
import com.guludoc.learning.u3app.uaa.util.JwtUtil;
import com.guludoc.learning.u3app.uaa.util.TotpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final TotpUtil totpUtil;

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
                    var userToSave = user.withPassword(passwordEncoder.encode(user.getPassword()))
                            .withMfaKey(totpUtil.encodeKeyToString());

                    return userRepository.save(userToSave);
                })
                .orElseThrow( () -> new UsernameNotFoundException("Can't find user"));
    }

    public Optional<User> findOptionalByUsernameAndPassword(String username, String password) {
        return userRepository.findUserByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public User upgradeMfaKey(User user) {
        User userToSave = user.withMfaKey(totpUtil.encodeKeyToString());
        return userRepository.save(userToSave);
    }

    public Optional<String> createTotp(String key) {
        return totpUtil.createTotp(key);
    }

    public Optional<User> findOptionalByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> findOptionalByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

//    @PreAuthorize("authentication.name == #user.username or hasRole('ADMIN')")
    @Transactional
    @RoleAdminOrSelfWithUserParam
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
