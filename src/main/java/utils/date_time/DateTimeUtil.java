package utils.date_time;

import lombok.experimental.UtilityClass;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtil {

    private static final int START_DAY_HOUR = 5;
    private static final int END_DAY_HOUR = 20;

    public static TimeOfDay determineTimeOfDay(long timestamp, int timezoneOffset) {
        var instant = Instant.ofEpochSecond(timestamp);
        var offset = ZoneOffset.ofTotalSeconds(timezoneOffset);
        var localTime = instant.atOffset(offset).toLocalTime();

        var hour = localTime.getHour();

        if (hour >= START_DAY_HOUR && hour <= END_DAY_HOUR) {
            return TimeOfDay.DAY;
        }

        return TimeOfDay.NIGHT;
    }

    public static String getTime(long timestamp, int timezoneOffset, String pattern) {
        var offset = ZoneOffset.ofTotalSeconds(timezoneOffset);
        var formatter = DateTimeFormatter.ofPattern(pattern);

        return Instant.ofEpochSecond(timestamp)
                .atOffset(offset)
                .toLocalTime()
                .format(formatter);
    }
}