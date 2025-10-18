package utils.wind;

import lombok.experimental.UtilityClass;
import java.util.NavigableMap;
import java.util.TreeMap;

@UtilityClass
public class BeaufortScaleMeasurer {

    private static final NavigableMap<Double, Integer> SCALE = new TreeMap<>();

    static {
        SCALE.put(0.3, 0);
        SCALE.put(1.5, 1);
        SCALE.put(3.9, 2);
        SCALE.put(5.0, 3);
        SCALE.put(7.0, 4);
        SCALE.put(10.0, 5);
        SCALE.put(13.0, 6);
        SCALE.put(17.0, 7);
        SCALE.put(20.0, 8);
        SCALE.put(24.0, 9);
        SCALE.put(28.0, 10);
        SCALE.put(32.0, 11);
        SCALE.put(Double.MAX_VALUE, 12);
    }

    public static int measure(double windSpeed) {
        if (windSpeed < 0) {
            throw new IllegalArgumentException("Invalid wind speed: " + windSpeed);
        }

        return SCALE.ceilingEntry(windSpeed).getValue();
    }
}