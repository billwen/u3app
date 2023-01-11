package com.guludoc.learning.u3app.uaa.security;

import com.guludoc.learning.u3app.uaa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

    private final UserRepository userRepository;

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        log.info("Updating password for user {}, new password {}", userDetails.getUsername(), newPassword);
        return userRepository.findUserByUsername(userDetails.getUsername())
                .map( user -> {
                    // TODO: make sure the password has been encoded.
                    return (UserDetails) userRepository.save(user.withPassword(newPassword));
                })
                .orElse(userDetails);

    }
}
