package exceptions;

public class EnvironmentVariableProcessException extends RuntimeException {

    private static final String MESSAGE = "Environment variable not found - %s";

    public EnvironmentVariableProcessException(String environmentVariable) {
        super(String.format(MESSAGE, environmentVariable));
    }
}
