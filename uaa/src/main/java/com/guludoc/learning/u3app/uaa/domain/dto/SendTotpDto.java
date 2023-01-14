package com.guludoc.learning.u3app.uaa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SendTotpDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String mfaId;

    @NotNull
    private MfaType type = MfaType.SMS;

    public enum MfaType {
        EMAIL,
        SMS;
    }
}
