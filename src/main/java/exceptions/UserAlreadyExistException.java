package exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends RuntimeException {

    public static final int HTTP_STATUS_CODE = HttpStatus.CONFLICT.value();
    public static final String MESSAGE = "User already exist";

    public UserAlreadyExistException() {
        super(MESSAGE);
    }
}
