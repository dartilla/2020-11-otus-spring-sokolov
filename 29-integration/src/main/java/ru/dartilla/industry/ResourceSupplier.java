package ru.dartilla.industry;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.dartilla.industry.resource.Coal;
import ru.dartilla.industry.resource.Iron;
import ru.dartilla.industry.resource.Wood;

@MessagingGateway
interface ResourceSupplier {

    @Gateway(requestChannel = "steelFactoryInChannel")
    void supplyCoal(Coal coal);

    @Gateway(requestChannel = "steelFactoryInChannel")
    void supplyWood(Wood wood);

    @Gateway(requestChannel = "steelFactoryInChannel")
    void supplyIron(Iron iron);
}
