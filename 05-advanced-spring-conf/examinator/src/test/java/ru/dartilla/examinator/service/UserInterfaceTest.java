package ru.dartilla.examinator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.dartilla.examinator.config.ExaminatorProps;
import ru.dartilla.examinator.config.localization.DefLocaleMessageSource;
import ru.dartilla.examinator.domain.ExamDetails;
import ru.dartilla.examinator.domain.Exercise;
import ru.dartilla.examinator.domain.ExerciseResult;
import ru.dartilla.examinator.domain.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("UserInterface ")
@SpringBootTest
class UserInterfaceTest {

    @Configuration
    @EnableConfigurationProperties(ExaminatorProps.class)
    @ComponentScan(basePackageClasses = ExaminatorProps.class)
    static class OnlyConfigConfiguration {
    }

    @Mock
    private InOut inOut;

    @Autowired
    private DefLocaleMessageSource messageSource;

    private UserInterface userInterface;
    private ByteArrayOutputStream outByteArrayStream;

    @BeforeEach
    private void setUp() {
        outByteArrayStream = new ByteArrayOutputStream(1000);
        userInterface = new UserInterface(inOut, messageSource);
    }

    @DisplayName("печатает вопрос")
    @Test
    public void printQuestion() {
        when(inOut.getOut()).thenReturn(new PrintStream(outByteArrayStream));
        Exercise exercise = new Exercise("Собака это?", null,
                Stream.of("Рыба", "Животное", "Гриб").collect(Collectors.toList()));
        userInterface.printQuestion(exercise);
        String outResult = new String(outByteArrayStream.toByteArray(), StandardCharsets.UTF_8);
        assertThat(outResult).contains(exercise.getQuestion());
        assertThat(outResult).contains(String.join(", ", exercise.getAnswersToChoose()));
    }

    @DisplayName("читает ответ")
    @Test
    public void readAnswer() {
        Set<String> answers = Stream.of("firstAnswer", "secondAnswer").collect(Collectors.toSet());
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream(String.join(", ", answers).getBytes())));
        assertThat(userInterface.readAnswer().containsAll(answers)).isTrue();
    }

    @DisplayName("не читает ответ с неправильным разделителем")
    @Test
    public void readAnswerWrongDelimiter() {
        Set<String> answers = Stream.of("firstAnswer", "secondAnswer").collect(Collectors.toSet());
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream(String.join(",", answers).getBytes())));
        assertThat(userInterface.readAnswer().containsAll(answers)).isFalse();
    }

    @DisplayName("выводит детали тестирования")
    @Test
    public void printExamDetails() {
        when(inOut.getOut()).thenReturn(new PrintStream(outByteArrayStream));
        ExamDetails examDetails = new ExamDetails(Arrays.asList(
                new ExerciseResult(new Exercise("Собака это?", null, null), false),
                new ExerciseResult(new Exercise("Щенок до какого возраста?", null, null), true)),
                3, new User("Артур", "Пирожков"));
        userInterface.printExamDetails(examDetails);
        String outResult = new String(outByteArrayStream.toByteArray(), StandardCharsets.UTF_8);
        System.out.println(outResult);
        assertThat(outResult).contains(messageSource.getMessage("ui.examDetails"));
        Matcher questionMatcher = Pattern.compile(messageSource.getMessage("ui.examDetails.question")).matcher(outResult);
        Matcher isAnswerCorrectMatcher = Pattern.compile(messageSource.getMessage("ui.examDetails.isAnsweredCorrect")).matcher(outResult);
        for (int i = 0; i < 2; i++) {
            assertThat(questionMatcher.find()).isTrue();
            assertThat(isAnswerCorrectMatcher.find()).isTrue();
        }
    }
}