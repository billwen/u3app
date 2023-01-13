package com.guludoc.learning.u3app.uaa.domain.dto;

import com.guludoc.learning.u3app.uaa.annotation.PasswordMatch;
import com.guludoc.learning.u3app.uaa.annotation.ValidEmail;
import com.guludoc.learning.u3app.uaa.annotation.ValidPassword;
import com.guludoc.learning.u3app.uaa.config.Constants;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@PasswordMatch
public class UserDto implements Serializable {
    @NotEmpty
    @Size(min = 4, max = 50, message = "The length of a valid username should between 4 and 50")
    private String username;

    @NotEmpty
    @ValidPassword
    private String password;

    @NotEmpty
    @ValidPassword
    private String matchPassword;

    @NotEmpty
//    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @ValidEmail
    private String email;

    private String name;

    @NotEmpty
    @Pattern(regexp = Constants.PATTERN_MOBILE)
    private String mobile;
}
