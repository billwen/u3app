package com.guludoc.learning.u3app.uaa.rest;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserResource {

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
