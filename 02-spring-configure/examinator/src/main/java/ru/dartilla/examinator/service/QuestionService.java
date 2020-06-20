package ru.dartilla.examinator.service;

import ru.dartilla.examinator.domain.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestions();
}
