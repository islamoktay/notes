package com.example.notes.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class NoProfanityValidator implements ConstraintValidator<NoProfanity, String> {

    // A simple list of banned words for demonstration purposes.
    private static final List<String> PROFANITIES = Arrays.asList("badword", "spam", "scam");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        
        String lowerCaseValue = value.toLowerCase();
        for (String profanity : PROFANITIES) {
            if (lowerCaseValue.contains(profanity)) {
                return false;
            }
        }
        return true;
    }
}
