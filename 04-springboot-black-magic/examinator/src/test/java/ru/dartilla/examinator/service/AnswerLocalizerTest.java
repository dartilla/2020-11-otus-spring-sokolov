package ru.dartilla.examinator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.examinator.config.localization.DefLocaleMessageSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnswerLocalizerTest {

    @Mock
    DefLocaleMessageSource messageSource;

    @Test
    public void perform() {
        AnswerLocalizer answerLocalizer = new AnswerLocalizer(messageSource);
        assertThat(answerLocalizer.localizeAnswer("anyQuestion", "25.3")).isEqualTo("25.3");
        assertThat(answerLocalizer.localizeAnswer("anyQuestion", "-22")).isEqualTo("-22");

        when(messageSource.getMessage("whoIsTheDaddy.answer.morgan")).thenReturn("Morgan");
        assertThat(answerLocalizer.localizeAnswer("whoIsTheDaddy", "morgan")).isEqualTo("Morgan");
        when(messageSource.getMessage("whoIsTheDaddy.answer.morgan")).thenReturn("Морган");
        assertThat(answerLocalizer.localizeAnswer("whoIsTheDaddy", "morgan")).isEqualTo("Морган");
    }
}