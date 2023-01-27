package com.guludoc.learning.u3app.uaa.rest;

import com.guludoc.learning.u3app.uaa.domain.User;
import com.guludoc.learning.u3app.uaa.domain.dto.UserProfileDto;
import com.guludoc.learning.u3app.uaa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminResource {

    private final UserService userService;

    @PutMapping("/users/{username}")
    public User updateUser(@PathVariable String username, @RequestBody UserProfileDto userProfileDto) {
        return userService.findOptionalByUsername(username)
                .map(user -> userService.saveUser(
                        user.withName(userProfileDto.getName())
                                .withEmail(userProfileDto.getEmail())
                                .withMobile(userProfileDto.getMobile())
                ))
                .orElseThrow();
    }
}
