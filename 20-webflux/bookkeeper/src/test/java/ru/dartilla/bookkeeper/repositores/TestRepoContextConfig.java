package ru.dartilla.bookkeeper.repositores;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.MongoConfig;

@SpringBootConfiguration
@Import(MongoConfig.class)
public class TestRepoContextConfig {
}
