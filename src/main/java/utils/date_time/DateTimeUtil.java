package utils.date_time;

import lombok.experimental.UtilityClass;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtil {

    private static final int START_DAY_HOUR = 5;
    private static final int END_DAY_HOUR = 20;

    public static TimeOfDay determineTimeOfDay(int timezoneOffset) {
        var currentTimestamp = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond();

        var offset = ZoneOffset.ofTotalSeconds(timezoneOffset);

        var hour = Instant.ofEpochSecond(currentTimestamp)
                .atOffset(offset)
                .toLocalTime()
                .getHour();

        if (hour >= START_DAY_HOUR && hour <= END_DAY_HOUR) {
            return TimeOfDay.DAY;
        }

        return TimeOfDay.NIGHT;
    }

    public static String getTime(long timestamp, int timezoneOffsetSeconds, String pattern) {
        var offset = ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds);
        var formatter = DateTimeFormatter.ofPattern(pattern);

        return Instant.ofEpochSecond(timestamp)
                .atOffset(offset)
                .toLocalTime()
                .format(formatter);
    }
}