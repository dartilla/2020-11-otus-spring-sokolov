package ru.dartilla.bookkeeper.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

@Component
public class ThursdayIsTodayHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        Map<String, DayOfWeek> details = Map.of("Day of week is", dayOfWeek);
        return DayOfWeek.THURSDAY == dayOfWeek ? Health.up().withDetails(details).build()
                : Health.down().withDetails(details).build();
    }
}
