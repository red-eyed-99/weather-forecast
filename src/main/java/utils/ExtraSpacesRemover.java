package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExtraSpacesRemover {

    public static String removeExtraSpaces(String locationName) {
        return locationName.trim().replaceAll("\\s{2,}", " ");
    }
}