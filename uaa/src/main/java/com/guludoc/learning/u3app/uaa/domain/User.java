package com.guludoc.learning.u3app.uaa.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private String username;

    private String password;

    private String email;

    private String name;
}
