package validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import validation.annotations.ValidUsername;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 20;

    private static final String PATTERN = "^[a-zA-Z\\d_]{5,20}$";

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username.isBlank()) {
            setCustomMessage(context, "Fill in the username field");
            return false;
        }

        if (username.length() < MIN_LENGTH || username.length() > MAX_LENGTH) {
            setCustomMessage(context, "Username must be between 5 and 20 characters");
            return false;
        }

        if (!username.matches(PATTERN)) {
            setCustomMessage(context, "Username must only consist of latin letters, numbers and underscores");
            return false;
        }

        return true;
    }

    private void setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
