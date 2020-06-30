package ru.dartilla.examinator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.examinator.config.localization.DefLocaleMessageSource;
import ru.dartilla.examinator.domain.Exercise;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInterfaceTest {

    @Mock
    private InOut inOut;

    @Mock
    private DefLocaleMessageSource messageSource;

    private UserInterface userInterface;
    private ByteArrayOutputStream outByteArrayStream;

    @BeforeEach
    private void setUp() {
        outByteArrayStream = new ByteArrayOutputStream(1000);
        userInterface = new UserInterface(inOut, messageSource);
    }

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

    @Test
    public void readAnswer() {
        Set<String> answers = Stream.of("firstAnswer", "secondAnswer").collect(Collectors.toSet());
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream(String.join(", ", answers).getBytes())));
        assertThat(userInterface.readAnswer().containsAll(answers)).isTrue();
    }

    @Test
    public void readAnswerWrongDelimiter() {
        Set<String> answers = Stream.of("firstAnswer", "secondAnswer").collect(Collectors.toSet());
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream(String.join(",", answers).getBytes())));
        assertThat(userInterface.readAnswer().containsAll(answers)).isFalse();
    }
}