package ru.dartilla.examinator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExamDetails {

    private final List<ExerciseResult> exerciseResults;
    private final int rightAnswersToPassExam;
    private final User user;
}
