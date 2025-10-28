package utils;

import exceptions.LocationNotFoundException;
import exceptions.OpenWeatherException;
import exceptions.UserAlreadyExistException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.ui.Model;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static utils.ModelAttributeUtil.ERROR_MESSAGE;

@UtilityClass
public class ExceptionHandler {

    public static String handle(RuntimeException exception, Model model, HttpServletResponse response,
                                String responsePage) {

        if (exception instanceof OpenWeatherException) {
            model.addAttribute(ERROR_MESSAGE, exception.getMessage());
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
            return responsePage;
        }

        if (exception instanceof LocationNotFoundException) {
            model.addAttribute(ERROR_MESSAGE, exception.getMessage());
            response.setStatus(SC_NOT_FOUND);
            return responsePage;
        }

        if (exception instanceof UserAlreadyExistException) {
            model.addAttribute(ERROR_MESSAGE, exception.getMessage());
            response.setStatus(SC_CONFLICT);
            return responsePage;
        }

        throw exception;
    }
}
