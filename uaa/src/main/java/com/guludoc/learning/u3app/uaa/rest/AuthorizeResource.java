package com.guludoc.learning.u3app.uaa.rest;

import com.guludoc.learning.u3app.uaa.domain.JwtTokens;
import com.guludoc.learning.u3app.uaa.domain.User;
import com.guludoc.learning.u3app.uaa.domain.dto.*;
import com.guludoc.learning.u3app.uaa.exception.DuplicateProblem;
import com.guludoc.learning.u3app.uaa.exception.InvalidTotpProblem;
import com.guludoc.learning.u3app.uaa.exception.UserNotEnabledProblem;
import com.guludoc.learning.u3app.uaa.service.EmailService;
import com.guludoc.learning.u3app.uaa.service.MessageService;
import com.guludoc.learning.u3app.uaa.service.UserCacheService;
import com.guludoc.learning.u3app.uaa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/authorize")
public class AuthorizeResource {

    private final UserService userService;

    private final UserCacheService cacheService;

    private final MessageService messageService;

    private final EmailService emailService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserDto userDto) {
        if (userService.isUsernameExisted(userDto.getUsername())) {
            throw new DuplicateProblem("Duplicated username");
        }

        if (userService.isEmailExisted(userDto.getEmail())) {
            throw new DuplicateProblem("Duplicated email address.");
        }

        if (userService.isMobileExisted(userDto.getMobile())) {
            throw new DuplicateProblem("Duplicated mobile");
        }

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
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) throws AuthenticationException {
        return userService.findOptionalByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword())
                .map( user -> {
                    User upgradedUser = user;
                    if (user.getMfaKey() == null || user.getMfaKey().length() == 0) {
                        // 升级用户
                        upgradedUser = userService.upgradeMfaKey(user);
                    }
                    // 1. 升级密码编码

                    // 2. 验证
                    if (!user.isEnabled() || !user.isAccountNonExpired() || !user.isAccountNonLocked() || !user.isCredentialsNonExpired() ) {
                        throw new UserNotEnabledProblem("User not enabled");
                    }

                    // 3. 判断 usingMfa，如果false，我们就直接返回Token
                    if (!user.isUsingMfa()) {
                        return ResponseEntity.ok().body(userService.login(loginDto.getUsername(), loginDto.getPassword()));
                    } else {
                        // 4 使用了mfa
                        // Save raw password in the object
                        upgradedUser = upgradedUser.withPassword(loginDto.getPassword());
                        String mfaId = cacheService.cacheUser(upgradedUser);
                        // 5 "X-Authenticate: mfa realm = mfaId"
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .header("X-Authenticate", "mfa", "realm=" + mfaId)
                                .build();
                    }
                })
                .orElseThrow( () -> new BadCredentialsException("用户名或密码错误"));

    }

    @PutMapping("/totp")
    public void setTotp(@Valid @RequestBody SendTotpDto sendTotpDto) {
        log.info("Send by {} to {}", sendTotpDto.getType().toString(), sendTotpDto.getMfaId());
        cacheService.retrieveUser(sendTotpDto.getMfaId())
                .flatMap(
                        user -> userService.createTotp(user.getMfaKey())
                                .map(totp -> Pair.of(user, totp))
                )
                .ifPresentOrElse(pair -> {
                    log.debug("totp: {}", pair.getRight());
                    if (sendTotpDto.getType() == MfaType.SMS) {
                        messageService.send(pair.getLeft().getMobile(), pair.getRight());
                    } else {
                        emailService.send(pair.getLeft().getEmail(), pair.getRight());
                    }
                }, () -> {
                    throw new InvalidTotpProblem("Invalid totp");
                });
    }

    @PostMapping("/totp")
    public JwtTokens verifyTotp(@Valid @RequestBody VerifyTotpDto verifyTotpDto) {
        return cacheService.verifyTotp(verifyTotpDto.getMfaId(), verifyTotpDto.getCode())
                .map( user -> userService.login(user.getUsername(), user.getPassword()))
                .orElseThrow( () -> new InvalidTotpProblem("Invalid totp"));
    }

    @PostMapping("/refresh")
    public JwtTokens refreshToken(@RequestHeader(name = "Authorization") String authorization, @RequestParam String refreshToken) throws AuthenticationException {
        String prefix = "Bearer ";
        String accessToken = authorization.replace(prefix, "");
        return userService.refresh( accessToken, refreshToken);
    }
}
