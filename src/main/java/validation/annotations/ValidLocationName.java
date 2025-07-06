package validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import validation.validators.LocationNameValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocationNameValidator.class)
public @interface ValidLocationName {

    String message() default "Invalid location name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}