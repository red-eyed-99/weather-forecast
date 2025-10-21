package controllers;

import dto.auth.SignInUserDTO;
import exceptions.BadCredentialsException;
import exceptions.LocationNotFoundException;
import exceptions.OpenWeatherException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static utils.ModelAttributeUtil.ERROR_MESSAGE;
import static utils.ModelAttributeUtil.USER;
import static utils.PagesUtil.HOME;
import static utils.PagesUtil.SEARCH_LOCATIONS;
import static utils.PagesUtil.SIGN_IN;

@ControllerAdvice
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

    @ExceptionHandler(OpenWeatherException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public String handleOpenWeatherException(OpenWeatherException exception, Model model) {
        model.addAttribute(ERROR_MESSAGE, exception.getMessage());
        return HOME;
    }

    @ExceptionHandler(LocationNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public String handleLocationNotFoundException(LocationNotFoundException exception, Model model) {
        model.addAttribute(ERROR_MESSAGE, exception.getMessage());
        return SEARCH_LOCATIONS;
    }
}
