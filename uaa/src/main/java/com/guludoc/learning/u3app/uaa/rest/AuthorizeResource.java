package com.guludoc.learning.u3app.uaa.rest;

import com.guludoc.learning.u3app.uaa.domain.JwtTokens;
import com.guludoc.learning.u3app.uaa.domain.dto.LoginDto;
import com.guludoc.learning.u3app.uaa.domain.dto.UserDto;
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
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        return userDto;
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
