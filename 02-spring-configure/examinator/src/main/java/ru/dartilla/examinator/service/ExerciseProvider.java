package ru.dartilla.examinator.service;

import ru.dartilla.examinator.domain.Excercise;

public interface ExerciseProvider {
    boolean hasNext();
    Excercise next();
    void close();
}
