package ru.dartilla.examinator.service;

import org.springframework.stereotype.Service;
import ru.dartilla.examinator.domain.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

@Service
public class ExerciseParserCsv  {

    /**
     * формат строки question,rightAnswerMark,answer[,answer]
     * Формат righAnswerMark: rightAnswerIndex[-rightAnswerIndex]
     */
    public Exercise parseLine(String nextLine) {
        String[] split = nextLine.split(",");
        if (split.length < 3) {
            throw new RuntimeException("Wrong format csv");
        }

        String questionContent = split[0];
        List<String> answerList = Stream.of(Arrays.copyOfRange(split, 2, split.length)).collect(
                toCollection(ArrayList::new));

        String rightAnswerMark = split[1];
        Set<String> rightAnswers = Stream.of(rightAnswerMark.split("-"))
                .map(indexStr -> answerList.get(Integer.parseInt(indexStr) - 1)).collect(toSet());

        return new Exercise(questionContent, rightAnswers, answerList.size() != 1 ? answerList : emptyList());
    }
}
