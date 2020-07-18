package ru.dartilla.examinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.dartilla.examinator.config.ExaminatorProps;

@SpringBootApplication
@EnableConfigurationProperties(ExaminatorProps.class)
public class ExaminatorApp {

    public static void main(String[] args) {
        SpringApplication.run(ExaminatorApp.class);
    }
}
