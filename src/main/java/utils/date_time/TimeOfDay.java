package utils.date_time;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TimeOfDay {

    DAY("day"),
    NIGHT("night");

    private final String value;
}