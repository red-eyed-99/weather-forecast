package exceptions;

import lombok.Getter;

@Getter
public class BadCredentialsException extends RuntimeException {

    private static final String MESSAGE = "Invalid username or password";

    private final String username;

    public BadCredentialsException(String username) {
        super(MESSAGE);
        this.username = username;
    }
}
