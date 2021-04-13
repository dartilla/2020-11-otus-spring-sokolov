package ru.dartilla.bookkeeper.mms;

import com.github.cloudyrock.spring.v5.EnableMongock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMongock
@Slf4j
public class MmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmsApplication.class);
        log.info("Исходные базы должны быть уже пролиты");
    }
}
