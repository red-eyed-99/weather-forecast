package utils.wind;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WindDirectionResolver {

    private static final int MIN_DEGREES = 0;
    private static final int MAX_DEGREES = 360;

    private static final int SECTOR_SIZE_DEGREES = 45;

    private static final WindDirection[] DIRECTIONS = {
            WindDirection.N,
            WindDirection.NE,
            WindDirection.E,
            WindDirection.SE,
            WindDirection.S,
            WindDirection.SW,
            WindDirection.W,
            WindDirection.NW
    };

    public static WindDirection resolve(int degrees) {
        if (degrees < MIN_DEGREES || degrees > MAX_DEGREES) {
            throw new IllegalArgumentException("Invalid degrees value: " + degrees);
        }

        int normalized = (degrees == MAX_DEGREES)
                ? MIN_DEGREES
                : degrees;

        int index = normalized / SECTOR_SIZE_DEGREES;

        return DIRECTIONS[index];
    }
}