package utils;

import lombok.experimental.UtilityClass;
import java.text.Normalizer;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtil {

    public static String removeExtraSpaces(String string) {
        return string.trim().replaceAll("\\s{2,}", " ");
    }

    public static String removeDiacritics(String string) {
        var normalized = Normalizer.normalize(string, Normalizer.Form.NFD);
        var pattern = Pattern.compile("\\p{M}");
        return pattern.matcher(normalized).replaceAll("");
    }
}