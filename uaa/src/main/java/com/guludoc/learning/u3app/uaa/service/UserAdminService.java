package com.guludoc.learning.u3app.uaa.service;

import com.guludoc.learning.u3app.uaa.annotation.RoleAdminOrRead;
import com.guludoc.learning.u3app.uaa.config.Constants;
import com.guludoc.learning.u3app.uaa.domain.Role;
import com.guludoc.learning.u3app.uaa.domain.User;
import com.guludoc.learning.u3app.uaa.domain.dto.CreateUserDto;
import com.guludoc.learning.u3app.uaa.repository.RoleRepository;
import com.guludoc.learning.u3app.uaa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAdminService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @RoleAdminOrRead
    public Page<User> findAll(Predicate predicate, Pageable pageable) {

    }

    public Optional<User> finalByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User createUser(final CreateUserDto createUserDto) {
        return roleRepository.findOptionalByRoleName(Constants.ROLE_STAFF)
                .map(role -> {
                    // Generate a default password
                    String password = "123";
                    User user = userRepository.save(User.builder()
                                    .username(createUserDto.getUsername())
                                    .email(createUserDto.getEmail())
                                    .mobile(createUserDto.getMobile())
                                    .name(createUserDto.getName())
                                    .usingMfa(createUserDto.isUsingMfa())
                            .build());
                })
                .orElseThrow();
    }
}
