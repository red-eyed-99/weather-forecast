package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static utils.PagesUtil.HOME;

@Controller
public class HomeController {

    @GetMapping
    public String showHomePage() {
        return HOME;
    }
}
