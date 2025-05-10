package validation.validators;

import dto.SignUpUserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import validation.annotations.PasswordMatches;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, SignUpUserDTO> {

    private static final String REPEAT_PASSWORD_FIELD_NAME = "repeatPassword";

    @Override
    public boolean isValid(SignUpUserDTO signUpUserDTO, ConstraintValidatorContext context) {
        var password = signUpUserDTO.getPassword();
        var repeatPassword = signUpUserDTO.getRepeatPassword();

        if (repeatPassword.isBlank()) {
            setCustomConstraintViolation(context, "Repeat password");
            return false;
        }

        if (!password.equals(repeatPassword)) {
            setCustomConstraintViolation(context);
            return false;
        }

        return true;
    }

    private void setCustomConstraintViolation(ConstraintValidatorContext context) {
        var defaultMessage = context.getDefaultConstraintMessageTemplate();

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(defaultMessage)
                .addPropertyNode(REPEAT_PASSWORD_FIELD_NAME)
                .addConstraintViolation();
    }

    private void setCustomConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(REPEAT_PASSWORD_FIELD_NAME)
                .addConstraintViolation();
    }
}
