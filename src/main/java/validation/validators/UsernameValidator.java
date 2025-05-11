package validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import validation.annotations.ValidUsername;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username.isBlank()) {
            setCustomMessage(context, "Fill in the username field");
            return false;
        }

        if (username.length() < 5 || username.length() > 20) {
            setCustomMessage(context, "Username must be between 5 and 20 characters");
            return false;
        }

        if (!username.matches("^[a-zA-Z\\d_]{5,20}$")) {
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
