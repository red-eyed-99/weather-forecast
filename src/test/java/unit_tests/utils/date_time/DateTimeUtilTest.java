package unit_tests.utils.date_time;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.date_time.DateTimeUtil;
import utils.date_time.TimeOfDay;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class DateTimeUtilTest {

    private static final int SECONDS_IN_HOUR = 3600;

    @ParameterizedTest
    @MethodSource("getDetermineTimeOfDayTestArguments")
    void shouldDetermineTimeOfDay(ZonedDateTime zonedDateTime, int timezoneOffset, TimeOfDay expectedTimeOfDay) {
        try (var zonedDateTimeMock = mockStatic(ZonedDateTime.class)) {

            zonedDateTimeMock.when(() -> ZonedDateTime.now(ZoneOffset.UTC))
                    .thenReturn(zonedDateTime);

            var actualTimeOfDay = DateTimeUtil.determineTimeOfDay(timezoneOffset);

            assertEquals(expectedTimeOfDay, actualTimeOfDay);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "2025-01-01T06:00:00, 3, 09:00",
            "2025-01-01T06:01:00, -3, 03:01",
            "2025-01-01T00:31:00, 18, 18:31",
            "2025-01-01T00:00:05, -18, 06:00"
    })
    void shouldReturnTime(LocalDateTime datetime, int timezoneOffset, String expectedTime) {
        var timestamp = datetime.toEpochSecond(ZoneOffset.UTC);
        var timezoneOffsetSeconds = timezoneOffset * SECONDS_IN_HOUR;

        var actualTime = DateTimeUtil.getTime(timestamp, timezoneOffsetSeconds, "HH:mm");

        assertEquals(expectedTime, actualTime);
    }

    static Stream<Arguments> getDetermineTimeOfDayTestArguments() {
        return Stream.of(
                Arguments.of(
                        ZonedDateTime.parse("2025-01-01T05:00:00.000000+03:00[Europe/Moscow]"),
                        10800, TimeOfDay.DAY
                ),
                Arguments.of(
                        ZonedDateTime.parse("2025-01-01T04:59:59.000000+03:00[Europe/Moscow]"),
                        10800, TimeOfDay.NIGHT
                ),
                Arguments.of(
                        ZonedDateTime.parse("2025-01-01T20:59:59.000000+03:00[Europe/Moscow]"),
                        10800, TimeOfDay.DAY
                ),
                Arguments.of(
                        ZonedDateTime.parse("2025-01-01T21:00:00.000000+03:00[Europe/Moscow]"),
                        10800, TimeOfDay.NIGHT
                )
        );
    }
}
