package ru.dartilla.examinator.service;

import org.springframework.stereotype.Service;
import ru.dartilla.examinator.config.localization.DefLocaleMessageSource;

import java.util.regex.Pattern;

@Service
public class AnswerLocalizer {

    private static final Pattern nonLettersOnly = Pattern.compile("^[^a-zA-Zа-яА-Я]+$");

    private final DefLocaleMessageSource messageSource;

    public AnswerLocalizer(DefLocaleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * локализуем только слова
     */
    public String localizeAnswer(String questionId, String answerId) {
        if (nonLettersOnly.matcher(answerId).find()) {
            return answerId;
        } else {
            return messageSource.getMessage(String.join(".", questionId, "answer", answerId));
        }
    }
}
