package com.guludoc.learning.u3app.uaa.rest;

import com.guludoc.learning.u3app.uaa.domain.User;
import com.guludoc.learning.u3app.uaa.domain.dto.UserProfileDto;
import com.guludoc.learning.u3app.uaa.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserResource {

    private final UserService userService;

    @PostMapping("/me")
    public User saveProfile(@Valid @RequestBody UserProfileDto userProfileDto, Principal principal) {
        return userService.findOptionalByUsername(principal.getName())
                .map(user -> userService.saveUser(user.withName(userProfileDto.getName())
                        .withEmail(userProfileDto.getEmail())
                        .withMobile(userProfileDto.getMobile())))
                .orElseThrow();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/by-email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.findOptionalByEmail(email).orElseThrow();
    }

    @GetMapping("/users/{username}")
    public String getCurrentUsername(@PathVariable String username) {
        return "hello. " + username;
    }

    @GetMapping("/users/manager")
    public String getManagerResource() {
        return "hello. ";
    }

    @GetMapping("/principal")
    public Authentication getPrincipal(Authentication auth) {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/greeting")
    public String greeting() {
       return "Hello World";
    }

    @PostMapping("/greeting")
    public String makeGreeting(@RequestParam String name) {
        return "Hello " + name;
    }

    @PutMapping("/greeting/{name}")
    public String putGreeting(@PathVariable String name) {
        return "Hello " + name;
    }

    @PostMapping("/greetingwithjson")
    @ResponseStatus(HttpStatus.CREATED)
    public String makeGreeting(@RequestBody Profile profile) {
        return "Hello " + profile.getGender();
    }

    @Data
    static class Profile {
        private String gender;

        private String idNo;
    }
}
