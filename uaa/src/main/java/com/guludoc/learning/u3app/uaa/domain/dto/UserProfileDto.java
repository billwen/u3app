package com.guludoc.learning.u3app.uaa.domain.dto;

import lombok.*;

@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileDto {

    private String name;

    private String email;

    private String mobile;
}
