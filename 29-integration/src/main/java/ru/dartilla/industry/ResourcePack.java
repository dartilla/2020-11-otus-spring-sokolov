package ru.dartilla.industry;

import lombok.Data;
import ru.dartilla.industry.resource.Fuel;
import ru.dartilla.industry.resource.Iron;

@Data
public class ResourcePack {
    private final Fuel fuel;
    private final Iron iron;
}
