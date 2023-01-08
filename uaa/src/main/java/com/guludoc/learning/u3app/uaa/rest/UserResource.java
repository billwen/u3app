package com.guludoc.learning.u3app.uaa.rest;

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
}
