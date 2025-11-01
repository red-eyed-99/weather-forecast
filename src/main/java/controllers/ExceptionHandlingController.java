package controllers;

import dto.auth.SignInUserDTO;
import exceptions.BadCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static utils.ModelAttributeUtil.ERROR_MESSAGE;
import static utils.ModelAttributeUtil.USER;
import static utils.PagesUtil.ERROR_404;
import static utils.PagesUtil.ERROR_500;
import static utils.PagesUtil.SEARCH_LOCATIONS;
import static utils.PagesUtil.SIGN_IN;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public String handleBadCredentialsException(BadCredentialsException exception, Model model) {
        var signInUserDto = new SignInUserDTO(exception.getUsername(), "");

        model.addAttribute(ERROR_MESSAGE, exception.getMessage());
        model.addAttribute(USER, signInUserDto);

        return SIGN_IN;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public String handleHandlerMethodValidationException(HandlerMethodValidationException exception, Model model) {
        var message = exception.getAllErrors()
                .getFirst()
                .getDefaultMessage();

        model.addAttribute(ERROR_MESSAGE, message);

        return SEARCH_LOCATIONS;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public void handleResponseStatusException(ResponseStatusException exception) {
        throw exception;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public String handleNoHandlerFoundException() {
        return ERROR_404;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public String handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ERROR_500;
    }
}
