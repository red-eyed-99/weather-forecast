package validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import validation.validators.UsernameValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface ValidUsername {

    String message() default "Incorrect username";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
