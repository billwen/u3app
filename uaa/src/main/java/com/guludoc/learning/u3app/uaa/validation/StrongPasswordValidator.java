package com.guludoc.learning.u3app.uaa.validation;

import com.guludoc.learning.u3app.uaa.annotation.ValidPassword;
import lombok.RequiredArgsConstructor;
import org.passay.*;
import org.passay.spring.SpringMessageResolver;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@RequiredArgsConstructor
public class StrongPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private final SpringMessageResolver resolver;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        var validator = new PasswordValidator(resolver, Arrays.asList(
                new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5,false),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),
                new WhitespaceRule()
        ));

        var result = validator.validate(new PasswordData(s));
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(String.join(",", validator.getMessages(result)))
                .addConstraintViolation();

        return result.isValid();
    }
}
