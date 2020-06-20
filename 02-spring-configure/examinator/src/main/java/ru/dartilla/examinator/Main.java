package ru.dartilla.examinator;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.dartilla.examinator.service.ExamService;

@Configuration
@ComponentScan
@PropertySource("classpath:examinator.properties")
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        ExamService examService = context.getBean(ExamService.class);
        examService.doExam();
    }
}
