package ru.dartilla.examinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.dartilla.examinator.config.ExaminatorProps;
import ru.dartilla.examinator.service.ExamService;

@SpringBootApplication
@EnableConfigurationProperties(ExaminatorProps.class)
public class ExaminatorApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ExaminatorApp.class);
        ExamService examService = context.getBean(ExamService.class);
        examService.doExam();
    }
}
