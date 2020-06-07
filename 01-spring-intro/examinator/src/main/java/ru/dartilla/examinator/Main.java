package ru.dartilla.examinator;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.dartilla.examinator.domain.Question;
import ru.dartilla.examinator.service.QuestionService;
import ru.dartilla.examinator.util.QuestionPrinter;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        QuestionService questionService = context.getBean(QuestionService.class);
        for (Question question : questionService.getQuestions()) {
            QuestionPrinter.printQuestion(question);
        }
    }
}
