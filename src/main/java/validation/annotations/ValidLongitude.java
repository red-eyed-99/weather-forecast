package validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotNull
@Digits(integer = 3, fraction = 4)
@DecimalMin(value = "-180")
@DecimalMax(value = "180")
public @interface ValidLongitude {

    String message() default "invalid longitude";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}