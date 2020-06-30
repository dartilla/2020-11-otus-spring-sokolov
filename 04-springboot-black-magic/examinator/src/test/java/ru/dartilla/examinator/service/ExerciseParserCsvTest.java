package ru.dartilla.examinator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.examinator.config.localization.DefLocaleMessageSource;
import ru.dartilla.examinator.domain.Exercise;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseParserCsvTest {

    @Mock
    private DefLocaleMessageSource messageSource;

    @Mock
    private AnswerLocalizer answerLocalizer;

    @Test
    public void parseLine() {

        when(messageSource.getMessage("possibleAggregateState.question"))
                .thenReturn("What aggregate state water could be?...");
        when(messageSource.getMessage("possibleAggregateState.answer.liquid"))
                .thenReturn("Liquid");
        when(messageSource.getMessage("possibleAggregateState.answer.gaseous"))
                .thenReturn("Gaseous");
        when(messageSource.getMessage("possibleAggregateState.answer.solid"))
                .thenReturn("Solid");

        when(answerLocalizer.localizeAnswer(anyString(), anyString())).thenAnswer(i
                -> messageSource.getMessage(i.getArguments()[0] + ".answer." + i.getArguments()[1]));

        ExerciseParserCsv parser = new ExerciseParserCsv(messageSource, answerLocalizer);
        Exercise exercise = parser.parseLine("possibleAggregateState,1-2-3,liquid,gaseous,solid");
        assertThat(exercise.getQuestion()).isEqualTo("What aggregate state water could be?...");
        assertThat(exercise.getAnswersToChoose().size()).isEqualTo(3);
        assertThat(exercise.getAnswersToChoose()).containsExactly("Liquid", "Gaseous", "Solid");
        assertThat(exercise.getRightAnswers().size()).isEqualTo(3);

        when(messageSource.getMessage("inventorOfRelativityTheory.question"))
                .thenReturn("Кто автор теории относительности?");
        when(messageSource.getMessage("inventorOfRelativityTheory.answer.einstein"))
                .thenReturn("Эйнштейн");
        parser = new ExerciseParserCsv(messageSource, answerLocalizer);
        exercise = parser.parseLine("inventorOfRelativityTheory,1,einstein");
        assertThat(exercise.getQuestion()).isEqualTo("Кто автор теории относительности?");
        assertThat(exercise.getAnswersToChoose().size()).isEqualTo(0);
        assertThat(exercise.getRightAnswers().iterator().next()).isEqualTo("Эйнштейн");
    }
}