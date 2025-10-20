package unit_tests.utils.wind;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import utils.wind.WindDirection;
import utils.wind.WindDirectionResolver;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WindDirectionResolverTest {

    @ParameterizedTest
    @MethodSource("getWindDirectionTestArguments")
    void shouldResolveWindDirection(int degrees, WindDirection expectedWindDirection) {
        var actualWindDirection = WindDirectionResolver.resolve(degrees);
        assertEquals(expectedWindDirection, actualWindDirection);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 361})
    void throwsIllegalArgumentException_whenDegreesIsIncorrect(int degrees) {
        assertThrows(
                IllegalArgumentException.class,
                () -> WindDirectionResolver.resolve(degrees)
        );
    }

    static Stream<Arguments> getWindDirectionTestArguments() {
        return Stream.of(
                Arguments.of(0, WindDirection.N),
                Arguments.of(360, WindDirection.N),
                Arguments.of(44, WindDirection.N),

                Arguments.of(45, WindDirection.NE),
                Arguments.of(89, WindDirection.NE),

                Arguments.of(90, WindDirection.E),
                Arguments.of(134, WindDirection.E),

                Arguments.of(135, WindDirection.SE),
                Arguments.of(179, WindDirection.SE),

                Arguments.of(180, WindDirection.S),
                Arguments.of(224, WindDirection.S),

                Arguments.of(225, WindDirection.SW),
                Arguments.of(269, WindDirection.SW),

                Arguments.of(270, WindDirection.W),
                Arguments.of(314, WindDirection.W),

                Arguments.of(315, WindDirection.NW),
                Arguments.of(359, WindDirection.NW)
        );
    }
}
