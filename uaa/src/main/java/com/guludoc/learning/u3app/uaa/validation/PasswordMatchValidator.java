package com.guludoc.learning.u3app.uaa.validation;

import com.guludoc.learning.u3app.uaa.annotation.PasswordMatch;
import com.guludoc.learning.u3app.uaa.domain.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserDto> {
    @Override
    public void initialize(PasswordMatch constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext constraintValidatorContext) {

        var result = userDto.getPassword().equals(userDto.getMatchPassword());
        return result;
    }
}
