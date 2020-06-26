package ru.dartilla.examinator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dartilla.examinator.domain.Excercise;

import java.util.*;

@Service
public class ExerciseProviderCsv implements ExerciseProvider {
    private final Scanner scanner;
    private final ExerciseParserCsv exerciseParser;

    public ExerciseProviderCsv(@Value("${questionAndAnswersFileResourcePath}") String questionAndAnswersFileName,
                               ExerciseParserCsv exerciseParser) {
        scanner = new Scanner(getClass().getResourceAsStream(questionAndAnswersFileName));
        this.exerciseParser = exerciseParser;
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNext();
    }

    @Override
    public Excercise next() {
        return exerciseParser.parseLine(scanner.nextLine());
    }

    @Override
    public void close() {
        scanner.close();
    }
}
