package com.guludoc.learning.u3app.uaa.domain.dto;

import com.guludoc.learning.u3app.uaa.annotation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    @NotEmpty
    @Size(min = 4, max = 50, message = "The length of a valid username should between 4 and 50")
    private String username;

    @NotEmpty
    @Size(min = 8, max = 20, message = "The length of a valid password should between 8 and 20")
    private String password;

    @NotEmpty
    @Size(min = 8, max = 20, message = "The length of a valid password should between 8 and 20")
    private String matchPassword;

    @NotEmpty
//    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @ValidEmail
    private String email;

    private String name;
}
