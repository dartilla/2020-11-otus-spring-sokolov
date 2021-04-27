package ru.dartilla.industry;

import lombok.extern.slf4j.Slf4j;
import ru.dartilla.industry.resource.Coal;
import ru.dartilla.industry.resource.Fuel;
import ru.dartilla.industry.resource.Iron;
import ru.dartilla.industry.resource.Wood;

@Slf4j
public class Steel {

    private Fuel fuel;
    private Iron iron;

    public Steel(Iron iron, Coal coal) {
        log.debug("Making steel from resources:{}, {}", iron, coal);
        this.fuel = coal;
        this.iron = iron;
    }

    public Steel(Iron iron, Wood wood) {
        log.debug("Making steel from resources:{}, {}", iron, wood);
        this.fuel = wood;
        this.iron = iron;
    }

    @Override
    public String toString() {
        return "Steel{" +
                "fuel=" + fuel +
                ", iron=" + iron +
                '}';
    }
}
