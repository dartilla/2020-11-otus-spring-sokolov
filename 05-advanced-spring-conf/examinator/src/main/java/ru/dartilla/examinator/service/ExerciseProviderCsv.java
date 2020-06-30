package ru.dartilla.examinator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dartilla.examinator.domain.Exercise;

import java.util.*;

@Service
public class ExerciseProviderCsv implements ExerciseProvider {
    private final ExerciseParserCsv exerciseParser;
    private final String questionAndAnswersFileName;

    private Scanner scanner;

    public ExerciseProviderCsv(
            @Value("${questionAndAnswersFileResourcePath}") String questionAndAnswersFileName,
            ExerciseParserCsv exerciseParser) {
        this.questionAndAnswersFileName = questionAndAnswersFileName;
        this.exerciseParser = exerciseParser;
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNext();
    }

    @Override
    public Exercise next() {
        return exerciseParser.parseLine(scanner.nextLine());
    }

    @Override
    public void close() {
        scanner.close();
    }

    @Override
    public void refresh() {
        scanner = new Scanner(getClass().getResourceAsStream(questionAndAnswersFileName));
    }
}
