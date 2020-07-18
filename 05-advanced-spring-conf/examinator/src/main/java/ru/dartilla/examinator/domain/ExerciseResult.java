package ru.dartilla.examinator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExerciseResult {
    private final Exercise exercise;
    private final boolean isAnsweredCorrect;
}
