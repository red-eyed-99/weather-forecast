package validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import validation.annotations.ValidPassword;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 20;

    private static final String PATTERN = "^[a-zA-Z\\d`~!@#№$%^&*()-_=+\\[\\]{}|;:'\",.]{5,20}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password.isBlank()) {
            setCustomMessage(context, "Fill in the password field");
            return false;
        }

        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            setCustomMessage(context, "Password must be between 5 and 20 characters");
            return false;
        }

        if (!password.matches(PATTERN)) {
            setCustomMessage(context,
                    "Password must only consist of latin letters, numbers and special characters without spaces");
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
