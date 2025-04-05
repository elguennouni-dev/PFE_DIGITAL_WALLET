package pfe.digitalWallet.shared.validation;

import jakarta.validation.ConstraintValidator;
import pfe.digitalWallet.shared.validation.annotation.ValidLocalDateTime;

import java.time.LocalDateTime;

public class LocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, LocalDateTime> {

    @Override
    public void initialize(ValidLocalDateTime constraintAnnotation) {

    }

    @Override
    public boolean isValid(LocalDateTime value, jakarta.validation.ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }
        return !value.isAfter(LocalDateTime.now());
    }

}
