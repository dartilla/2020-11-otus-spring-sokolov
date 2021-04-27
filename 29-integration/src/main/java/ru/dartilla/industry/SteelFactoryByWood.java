package ru.dartilla.industry;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dartilla.industry.resource.Wood;

@Service
@Slf4j
public class SteelFactoryByWood {

    @SneakyThrows
    public Steel makeSteel(ResourcePack resourcePack) {
        Thread.sleep(1000);
        return new Steel(resourcePack.getIron(), (Wood) resourcePack.getFuel());
    }
}
