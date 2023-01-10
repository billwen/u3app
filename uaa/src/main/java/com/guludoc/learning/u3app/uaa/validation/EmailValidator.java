package com.guludoc.learning.u3app.uaa.validation;

import com.guludoc.learning.u3app.uaa.annotation.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private  final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return validateEmail(s);
    }

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

    }

    private boolean validateEmail(final String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
