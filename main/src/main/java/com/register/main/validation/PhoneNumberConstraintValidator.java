package com.register.main.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberConstraintValidator implements ConstraintValidator<PhoneNumber, String> {

    private String prefix;

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        this.prefix = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are often handled by @NotBlank
        }
        return value.startsWith(prefix) && value.matches("\\d{10}");
    }
}