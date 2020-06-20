package ru.dartilla.examinator.util;

import org.junit.Assert;
import org.junit.Test;
import ru.dartilla.examinator.domain.Answer;
import ru.dartilla.examinator.domain.Question;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class QuestionPrinterTest {

    @Test
    public void printQuestion() {
        String string = QuestionPrinter.printQuestion(new Question("Собака это?", null,
                Stream.of("Рыба", "Животное", "Гриб").map(Answer::new).collect(Collectors.toList())));
        Assert.assertEquals("Собака это?" + System.lineSeparator() + "\tРыба, Животное, Гриб", string);
    }
}