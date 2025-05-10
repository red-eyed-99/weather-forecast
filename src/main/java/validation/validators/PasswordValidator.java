package validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import validation.annotations.ValidPassword;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password.isBlank()) {
            setCustomMessage(context, "Fill in the password field");
            return false;
        }

        if (password.length() < 5 || password.length() > 20) {
            setCustomMessage(context, "Password must be between 5 and 20 characters");
            return false;
        }

        if (!password.matches("^[a-zA-Z\\d`~!@#№$%^&*()-_=+\\[\\]{}|;:'\",.]{5,20}$")) {
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
