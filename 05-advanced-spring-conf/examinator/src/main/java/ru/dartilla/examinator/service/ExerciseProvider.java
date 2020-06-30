package ru.dartilla.examinator.service;

import ru.dartilla.examinator.domain.Exercise;

public interface ExerciseProvider {
    boolean hasNext();
    Exercise next();
    void close();
}
