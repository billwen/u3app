package com.guludoc.learning.u3app.uaa.rest;

import com.guludoc.learning.u3app.uaa.domain.JwtTokens;
import com.guludoc.learning.u3app.uaa.domain.User;
import com.guludoc.learning.u3app.uaa.domain.dto.LoginDto;
import com.guludoc.learning.u3app.uaa.domain.dto.UserDto;
import com.guludoc.learning.u3app.uaa.exception.DuplicateProblem;
import com.guludoc.learning.u3app.uaa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authorize")
public class AuthorizeResource {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserDto userDto) {
        // TODO: 1. 检查username， email， mobile都是唯一的，
        if (userService.isUsernameExisted(userDto.getUsername())) {
            throw new DuplicateProblem("Duplicated username");
        }

        if (userService.isEmailExisted(userDto.getEmail())) {
            throw new DuplicateProblem("Duplicated email address.");
        }

        if (userService.isMobileExisted(userDto.getMobile())) {
            throw new DuplicateProblem("Duplicated mobile");
        }

        // TODO: 2. UserDto to User，给一个默认角色，然后保存
        var user = User.builder()
                .username(userDto.getUsername())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .mobile(userDto.getMobile())
                .password(userDto.getPassword())
                .build();

        return userService.register(user);
    }

    @PostMapping("/token")
    public JwtTokens login(@Valid @RequestBody LoginDto loginDto) throws AuthenticationException {
        return userService.login(loginDto.getUsername(), loginDto.getPassword());
    }

    @PostMapping("/refresh")
    public JwtTokens refreshToken(@RequestHeader(name = "Authorization") String authorization, @RequestParam String refreshToken) throws AuthenticationException {
        String prefix = "Bearer ";
        String accessToken = authorization.replace(prefix, "");
        return userService.refresh( accessToken, refreshToken);
    }
}
