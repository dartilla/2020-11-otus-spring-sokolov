package ru.dartilla.examinator.service;

import org.junit.jupiter.api.Test;
import ru.dartilla.examinator.domain.Exercise;

import static org.assertj.core.api.Assertions.assertThat;

public class ExerciseParserCsvTest {

    @Test
    public void parseLine() {
        ExerciseParserCsv parser = new ExerciseParserCsv();
        Exercise exercise = parser.parseLine("Вода бывает в виде...,1-2-3,Жидкая,Газообразная,Твердая");
        assertThat(exercise.getQuestion()).isEqualTo("Вода бывает в виде...");
        assertThat(exercise.getAnswersToChoose().size()).isEqualTo(3);
        assertThat(exercise.getRightAnswers().size()).isEqualTo(3);

        exercise = parser.parseLine("Вместе весело шагать по...,1,просторам");
        assertThat(exercise.getQuestion()).isEqualTo("Вместе весело шагать по...");
        assertThat(exercise.getAnswersToChoose().size()).isEqualTo(0);
        assertThat(exercise.getRightAnswers().iterator().next()).isEqualTo("просторам");
    }
}