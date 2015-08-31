package personal.dvinov.calendar.service.core.trainers.business;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;

public class WeekdayNineToFiveHourLongEligibleSlotProvider implements Iterable<SlotBusinessObject> {
    private final ZoneId zoneId;
    private final LocalDateTime queryIntervalStart;
    private final LocalDateTime queryIntervalEnd;
    
    public WeekdayNineToFiveHourLongEligibleSlotProvider(
            ZoneId zoneId,
            LocalDateTime queryIntervalStart,
            LocalDateTime queryIntervalEnd) {

        this.zoneId = zoneId;
        this.queryIntervalStart = queryIntervalStart;
        this.queryIntervalEnd = queryIntervalEnd;
    }

    @Override
    public Iterator<SlotBusinessObject> iterator() {
        return new EligibleSlotIterator(
                EligibleSlotConfiguration.WEEKDAYS_9_TO_5,
                zoneId, queryIntervalStart, queryIntervalEnd);
    }
}
