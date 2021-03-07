package ru.dartilla.bookkeeper.config;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
@EnableMongock
public class MongoConfig {
}
