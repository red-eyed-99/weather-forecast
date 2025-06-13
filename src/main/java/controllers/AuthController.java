package controllers;

import dto.SignInUserDTO;
import dto.SignUpUserDTO;
import dto.UserSessionDTO;
import exceptions.UserAlreadyExistException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import models.entities.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import services.AuthService;
import utils.CookieUtil;
import utils.PasswordEncoder;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static utils.ModelAttributeUtil.ERROR_MESSAGE;
import static utils.ModelAttributeUtil.PASSWORD_ERROR;
import static utils.ModelAttributeUtil.REPEAT_PASSWORD_ERROR;
import static utils.ModelAttributeUtil.USER;
import static utils.ModelAttributeUtil.USERNAME_ERROR;
import static utils.ModelAttributeUtil.USER_SESSION;
import static utils.PagesUtil.REDIRECT_HOME;
import static utils.PagesUtil.SIGN_IN;
import static utils.PagesUtil.SIGN_UP;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/sign-up")
    public String showSignUpPage(Model model) {
        if (model.containsAttribute(USER_SESSION)) {
            return REDIRECT_HOME;
        }

        return SIGN_UP;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(CREATED)
    public String signUp(Model model, @ModelAttribute(USER) @Valid SignUpUserDTO signUpUserDTO,
                         BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            addSignUpValidationErrors(model, bindingResult);
            response.setStatus(SC_BAD_REQUEST);
            return SIGN_UP;
        }

        model.addAttribute(USER, getCopy(signUpUserDTO));

        encodePassword(signUpUserDTO);

        UserSession userSession;

        try {
            userSession = authService.signUp(signUpUserDTO);
        } catch (UserAlreadyExistException exception) {
            model.addAttribute(ERROR_MESSAGE, exception.getMessage());
            response.setStatus(SC_CONFLICT);
            return SIGN_UP;
        }

        CookieUtil.addUserSessionCookie(userSession, response);

        return REDIRECT_HOME;
    }

    private SignUpUserDTO getCopy(SignUpUserDTO signUpUserDTO) {
        return new SignUpUserDTO(
                signUpUserDTO.getUsername(),
                signUpUserDTO.getPassword(),
                signUpUserDTO.getRepeatPassword()
        );
    }

    @GetMapping("/sign-in")
    public String showSignInPage(Model model) {
        if (model.containsAttribute(USER_SESSION)) {
            return REDIRECT_HOME;
        }

        return SIGN_IN;
    }

    @PostMapping("/sign-in")
    public String signIn(Model model, @ModelAttribute(USER) @Valid SignInUserDTO signInUserDTO,
                         BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            addSignInValidationErrors(model, bindingResult);
            response.setStatus(SC_BAD_REQUEST);
            return SIGN_IN;
        }

        var userSession = authService.signIn(signInUserDTO);

        CookieUtil.addUserSessionCookie(userSession, response);

        return REDIRECT_HOME;
    }

    private void addSignUpValidationErrors(Model model, BindingResult bindingResult) {
        model.addAttribute(USERNAME_ERROR, bindingResult.getFieldError(USERNAME));
        model.addAttribute(PASSWORD_ERROR, bindingResult.getFieldError(PASSWORD));
        model.addAttribute(REPEAT_PASSWORD_ERROR, bindingResult.getFieldError(REPEAT_PASSWORD));
    }

    private void addSignInValidationErrors(Model model, BindingResult bindingResult) {
        model.addAttribute(USERNAME_ERROR, bindingResult.getFieldError(USERNAME));
        model.addAttribute(PASSWORD_ERROR, bindingResult.getFieldError(PASSWORD));
    }

    private void encodePassword(SignUpUserDTO signUpUserDTO) {
        var encodedPassword = PasswordEncoder.encode(signUpUserDTO.getPassword());
        signUpUserDTO.setEncryptedPassword(encodedPassword);
    }

    @PostMapping("/logout")
    public String logout(Model model, HttpServletResponse response) {
        if (model.containsAttribute(USER_SESSION)) {
            var userSessionDto = (UserSessionDTO) model.getAttribute(USER_SESSION);

            authService.logout(userSessionDto);

            CookieUtil.deleteUserSessionCookie(response);
        }

        return REDIRECT_HOME;
    }
}
