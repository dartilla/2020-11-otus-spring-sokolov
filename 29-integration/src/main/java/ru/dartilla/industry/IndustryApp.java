package ru.dartilla.industry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import ru.dartilla.industry.resource.Coal;
import ru.dartilla.industry.resource.Fuel;
import ru.dartilla.industry.resource.Iron;
import ru.dartilla.industry.resource.Wood;

import java.util.function.Supplier;
import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
public class IndustryApp {

    /**
     * Через канал steelFactoryInChannel идут поставки ресурсов для выплавки стали (Железо, Уголь, Дерево)
     * Есть сталелитейная использующая дерево в качестве топлива (SteelFactoryByCoal) и есть угольная (SteelFactoryByCoal)
     * Настроен процесс makeSteelFlow, который распределяет ресурсы по фабрикам и забирает готовую сталь в канал steelFactoryOutChannel
     */
    public static void main(String[] args) {
        SpringApplication.run(IndustryApp.class);

        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(IndustryApp.class);
        ResourceSupplier resourceSupplier = ctx.getBean(ResourceSupplier.class);

        resourceSupplier.supplyCoal(new Coal(1));
        resourceSupplier.supplyIron(new Iron(1));
        resourceSupplier.supplyWood(new Wood(1));
        resourceSupplier.supplyCoal(new Coal(2));
        resourceSupplier.supplyIron(new Iron(2));
        resourceSupplier.supplyIron(new Iron(3));

        QueueChannel steelFactoryOutChannel = ctx.getBean("steelFactoryOutChannel", QueueChannel.class);

        Runnable steelReceiver = () -> {
            while (true) {
                Steel payload = (Steel) steelFactoryOutChannel.receive().getPayload();
                log.debug("Recieved {}", payload);
            }
        };
        steelReceiver.run();
    }

    @Bean
    public IntegrationFlow makeSteelFlow() {
        return IntegrationFlows.from("steelFactoryInChannel")
                .aggregate(x -> x
                        .correlationStrategy(new CorrelationStrategy() {
                            private long fuelCount = 0L;
                            private long ironCount = 0L;

                            @Override
                            public Object getCorrelationKey(Message<?> message) {
                                if (message.getPayload() instanceof Fuel) {
                                    return ++fuelCount;
                                } else if (message.getPayload() instanceof Iron) {
                                    return ++ironCount;
                                } else {
                                    throw new IllegalArgumentException("Wrong payload=" + message.getPayload());
                                }
                            }
                        }).releaseStrategy(messageGroup -> messageGroup.getMessages().size() == 2)
                        .outputProcessor(messageGroup -> {
                            Supplier<Stream<?>> payloads = () -> messageGroup.getMessages().stream().map(Message::getPayload);
                            Fuel fuel = (Fuel) payloads.get().filter(y -> y instanceof Fuel).findAny().orElseThrow(
                                    () -> new IllegalArgumentException("Not found Fuel"));
                            Iron iron = (Iron) payloads.get().filter(y -> y instanceof Iron).findAny().orElseThrow(
                                    () -> new IllegalArgumentException("Not found Iron")
                            );
                            return new ResourcePack(fuel, iron);
                        })
                )
                .<ResourcePack, Class<? extends Fuel>>route(rPack -> rPack.getFuel().getClass(), mapping -> mapping
                        .subFlowMapping(Coal.class, sub -> sub
                                .handle("steelFactoryByCoal", "makeSteel")
                        ).subFlowMapping(Wood.class, sub -> sub
                                .handle("steelFactoryByWood", "makeSteel")
                        )
                )
                .channel("steelFactoryOutChannel")
                .get();
    }

    @Bean
    public QueueChannel steelFactoryInChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public QueueChannel steelFactoryOutChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate( 100 ).maxMessagesPerPoll( 2 ).get();
    }
}