package com.guludoc.learning.u3app.uaa.rest;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserResource {

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
    public String makeGreeting(@RequestBody Profile profile) {
        return "Hello " + profile.getGender();
    }

    @Data
    static class Profile {
        private String gender;

        private String idNo;
    }
}
