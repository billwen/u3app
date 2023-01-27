package com.guludoc.learning.u3app.uaa.domain.dto;

import lombok.Data;

@Data
public class CreateUserDto {

    private String username;

    private String password;

    private String email;

    private String mobile;

    private String name;

    private boolean usingMfa;

}
