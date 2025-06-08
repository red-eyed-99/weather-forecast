package controllers;

import dto.SignInUserDTO;
import exception.BadCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static utils.ModelAttributeUtil.ERROR_MESSAGE;
import static utils.ModelAttributeUtil.USER;
import static utils.PagesUtil.SIGN_IN;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleBadCredentials(BadCredentialsException exception, Model model) {
        var signInUserDto = new SignInUserDTO(exception.getUsername(), "");

        model.addAttribute(ERROR_MESSAGE, exception.getMessage());
        model.addAttribute(USER, signInUserDto);

        return SIGN_IN;
    }
}
