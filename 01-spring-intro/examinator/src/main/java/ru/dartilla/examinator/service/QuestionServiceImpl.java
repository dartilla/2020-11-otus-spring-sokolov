package ru.dartilla.examinator.service;

import ru.dartilla.examinator.domain.Answer;
import ru.dartilla.examinator.domain.Question;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;

public class QuestionServiceImpl implements QuestionService {
    private final String questionAndAnswersFileName;

    public QuestionServiceImpl(String questionAndAnswersFileName) {
        this.questionAndAnswersFileName = questionAndAnswersFileName;
    }

    @Override
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        Scanner scanner = new Scanner(getClass().getResourceAsStream(questionAndAnswersFileName));
        while (scanner.hasNext()) {
            questions.add(parseLine(scanner.nextLine()));
        }
        scanner.close();
        return questions;
    }

    /**
     * Формат строки: question,rightAnswersMark,answers..
     */
    Question parseLine(String nextLine) {
        String[] split = nextLine.split(",");
        if (split.length < 3) {
            throw new RuntimeException("Wrong format csv");
        }

        String questionContent = split[0];
        List<Answer> answerList = Stream.of(Arrays.copyOfRange(split, 2, split.length)).map(Answer::new).collect(
                toCollection(ArrayList::new));

        String rightAnswerMark = split[1];//Формат righAnswerMark: rightAnswerIndex[-rightAnswerIndex]
        Set<Answer> rightAnswers = Stream.of(rightAnswerMark.split("-"))
                .map(indexStr -> answerList.get(Integer.parseInt(indexStr) - 1)).collect(toSet());

        return new Question(questionContent, rightAnswers, answerList.size() != 1 ? answerList : emptyList());
    }
}
