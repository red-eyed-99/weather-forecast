package validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import utils.ExtraSpacesRemover;
import validation.annotations.ValidLocationName;

public class LocationNameValidator implements ConstraintValidator<ValidLocationName, String> {

    private static final int MAX_LENGTH = 100;

    private static final String PATTERN = "^(?=.*[A-Za-z])[A-Za-z\\d\\s\\-,.’'&()/]{1,100}$";

    @Override
    public boolean isValid(String locationName, ConstraintValidatorContext context) {
        locationName = ExtraSpacesRemover.removeExtraSpaces(locationName);

        if (locationName.isBlank()) {
            setCustomMessage(context, "Enter location name. Example: Moscow");
            return false;
        }

        if (locationName.length() > MAX_LENGTH) {
            setCustomMessage(context, "Location name is too long");
            return false;
        }

        if (!locationName.matches(PATTERN)) {
            setCustomMessage(context, "Enter at least 1 letter. " +
                    "Allowed latin characters, numbers, spaces, special characters -,.’'&()/.");
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