package com.guludoc.learning.u3app.uaa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VerifyTotpDto implements Serializable {
    @NotEmpty
    private String mfaId;

    @NotEmpty
    private String code;
}
