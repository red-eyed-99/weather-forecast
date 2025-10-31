package utils;

import lombok.experimental.UtilityClass;

import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

@UtilityClass
public class PagesUtil {

    public static final String HOME = "home/index";

    public static final String SIGN_UP = "sign-up/sign-up";
    public static final String SIGN_IN = "sign-in/sign-in";

    public static final String SEARCH_LOCATIONS = "search-locations/search-locations";

    public static final String REDIRECT_HOME = REDIRECT_URL_PREFIX + "/";

    public static final String ERROR_500 = "errors/500.html";
    public static final String ERROR_404 = "errors/404.html";
}