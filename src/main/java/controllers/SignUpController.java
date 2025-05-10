package controllers;

import dto.SignUpUserDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.UserService;
import utils.PasswordEncoder;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static utils.ModelAttributeUtil.PASSWORD_ERROR;
import static utils.ModelAttributeUtil.REPEAT_PASSWORD_ERROR;
import static utils.ModelAttributeUtil.USER;
import static utils.ModelAttributeUtil.USERNAME_ERROR;
import static utils.ModelAttributeUtil.USER_SESSION;
import static utils.PagesUtil.REDIRECT_HOME;
import static utils.PagesUtil.SIGN_UP;

@Controller
@RequestMapping("/sign-up")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @GetMapping
    public String showSignUpPage(Model model) {
        if (model.containsAttribute(USER_SESSION)) {
            return REDIRECT_HOME;
        }

        return SIGN_UP;
    }

    @PostMapping
    public String signUp(Model model, @ModelAttribute(USER) @Valid SignUpUserDTO signUpUserDTO,
                         BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            addValidationErrors(model, bindingResult);
            response.setStatus(SC_BAD_REQUEST);
            return SIGN_UP;
        }

        encodePasswords(signUpUserDTO);

        userService.signUp(signUpUserDTO);

        return REDIRECT_HOME;
    }

    private void addValidationErrors(Model model, BindingResult bindingResult) {
        model.addAttribute(USERNAME_ERROR, bindingResult.getFieldError("username"));
        model.addAttribute(PASSWORD_ERROR, bindingResult.getFieldError("password"));
        model.addAttribute(REPEAT_PASSWORD_ERROR, bindingResult.getFieldError("repeatPassword"));
    }

    private void encodePasswords(SignUpUserDTO signUpUserDTO) {
        var encodedPassword = PasswordEncoder.encode(signUpUserDTO.getPassword());
        var encodedRepeatPassword = PasswordEncoder.encode(signUpUserDTO.getRepeatPassword());

        signUpUserDTO.setPassword(encodedPassword);
        signUpUserDTO.setRepeatPassword(encodedRepeatPassword);
    }
}
