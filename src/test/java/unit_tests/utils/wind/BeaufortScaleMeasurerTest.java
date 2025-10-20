package unit_tests.utils.wind;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.wind.BeaufortScaleMeasurer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeaufortScaleMeasurerTest {

    @ParameterizedTest
    @MethodSource("getBeaufortPointsTestArguments")
    void shouldDetermineBeaufortPoints(double windSpeed, int expectedBeaufortPoints) {
        var actualBeaufortPoints = BeaufortScaleMeasurer.measure(windSpeed);
        assertEquals(expectedBeaufortPoints, actualBeaufortPoints);
    }

    @Test
    void throwsIllegalArgumentException_whenWindSpeedIsNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> BeaufortScaleMeasurer.measure(-0.1)
        );
    }

    static Stream<Arguments> getBeaufortPointsTestArguments() {
        return Stream.of(
                Arguments.of(0.0, 0),
                Arguments.of(0.2, 0),
                Arguments.of(0.3, 0),

                Arguments.of(0.4, 1),
                Arguments.of(1.4, 1),
                Arguments.of(1.5, 1),

                Arguments.of(1.6, 2),
                Arguments.of(3.8, 2),
                Arguments.of(3.9, 2),

                Arguments.of(4.0, 3),
                Arguments.of(4.9, 3),
                Arguments.of(5.0, 3),

                Arguments.of(5.1, 4),
                Arguments.of(6.9, 4),
                Arguments.of(7.0, 4),

                Arguments.of(7.1, 5),
                Arguments.of(9.9, 5),
                Arguments.of(10.0, 5),

                Arguments.of(10.1, 6),
                Arguments.of(12.9, 6),
                Arguments.of(13.0, 6),

                Arguments.of(13.1, 7),
                Arguments.of(16.9, 7),
                Arguments.of(17.0, 7),

                Arguments.of(17.1, 8),
                Arguments.of(19.9, 8),
                Arguments.of(20.0, 8),

                Arguments.of(20.1, 9),
                Arguments.of(23.9, 9),
                Arguments.of(24.0, 9),

                Arguments.of(24.1, 10),
                Arguments.of(27.9, 10),
                Arguments.of(28.0, 10),

                Arguments.of(28.1, 11),
                Arguments.of(31.9, 11),
                Arguments.of(32.0, 11),

                Arguments.of(32.1, 12),
                Arguments.of(Double.MAX_VALUE, 12)
        );
    }
}
