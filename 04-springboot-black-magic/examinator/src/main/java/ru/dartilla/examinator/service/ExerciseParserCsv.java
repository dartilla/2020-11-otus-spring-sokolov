package ru.dartilla.examinator.service;

import org.springframework.stereotype.Service;
import ru.dartilla.examinator.config.localization.DefLocaleMessageSource;
import ru.dartilla.examinator.domain.Exercise;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

@Service
public class ExerciseParserCsv  {

    private final DefLocaleMessageSource messageSource;
    private final AnswerLocalizer answerLocalizer;

    public ExerciseParserCsv(DefLocaleMessageSource messageSource, AnswerLocalizer answerLocalizer) {
        this.messageSource = messageSource;
        this.answerLocalizer = answerLocalizer;
    }

    /**
     * формат строки questionId,rightAnswerMark,answerId[,answerId]
     * Формат righAnswerMark: rightAnswerIndex[-rightAnswerIndex]
     */
    public Exercise parseLine(String nextLine) {
        String[] split = nextLine.split(",");
        if (split.length < 3) {
            throw new RuntimeException("Wrong format csv");
        }

        String questionId = split[0];
        String questionContent = messageSource.getMessage(questionId + ".question");
        List<String> answerList = Stream.of(Arrays.copyOfRange(split, 2, split.length)).map(answerId
                -> answerLocalizer.localizeAnswer(questionId, answerId)).collect(toCollection(ArrayList::new));

        String rightAnswerMark = split[1];
        Set<String> rightAnswers = Stream.of(rightAnswerMark.split("-"))
                .map(indexStr -> answerList.get(Integer.parseInt(indexStr) - 1)).collect(toSet());

        return new Exercise(questionContent, rightAnswers, answerList.size() != 1 ? answerList : emptyList());
    }
}
