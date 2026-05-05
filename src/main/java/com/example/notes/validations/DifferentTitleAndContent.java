package com.example.notes.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DifferentTitleAndContentValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentTitleAndContent {
    String message() default "{validation.note.different_title_content}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
