package ru.dartilla.examinator.service;

import org.junit.jupiter.api.Test;
import ru.dartilla.examinator.domain.Excercise;

import static org.assertj.core.api.Assertions.assertThat;

public class ExerciseParserCsvTest {

    @Test
    public void parseLine() {
        ExerciseParserCsv parser = new ExerciseParserCsv();
        Excercise excercise = parser.parseLine("Вода бывает в виде...,1-2-3,Жидкая,Газообразная,Твердая");
        assertThat(excercise.getQuestion()).isEqualTo("Вода бывает в виде...");
        assertThat(excercise.getAnswersToChoose().size()).isEqualTo(3);
        assertThat(excercise.getRightAnswers().size()).isEqualTo(3);

        excercise = parser.parseLine("Вместе весело шагать по...,1,просторам");
        assertThat(excercise.getQuestion()).isEqualTo("Вместе весело шагать по...");
        assertThat(excercise.getAnswersToChoose().size()).isEqualTo(0);
        assertThat(excercise.getRightAnswers().iterator().next()).isEqualTo("просторам");
    }
}