package personal.dvinov.calendar.service.api.common;

import java.time.ZoneId;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class LocationConfiguration {

    private final Map<String, ZoneId> locationToTimeZone = ImmutableMap.of(
            "Seattle", ZoneId.of("America/Los_Angeles")
    );

    public ZoneId getZoneId(final String location) {
        return locationToTimeZone.get(location);
    }
}
