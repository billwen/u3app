package com.guludoc.learning.u3app.uaa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokens implements Serializable {

    private String accessToken;

    private String refreshToken;

    public static JwtTokens empty() {
        return new JwtTokens("", "");
    }
}
