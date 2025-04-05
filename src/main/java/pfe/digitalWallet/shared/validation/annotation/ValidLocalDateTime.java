package pfe.digitalWallet.shared.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pfe.digitalWallet.shared.validation.LocalDateTimeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateTimeValidator.class)
public @interface ValidLocalDateTime {
    String message() default "Invalid date-time, must not be null or in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
