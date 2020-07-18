package ru.dartilla.examinator.service;

import ru.dartilla.examinator.domain.User;

public interface ExamService {
    void doExam();

    void doExam(User user);

    void printDetail();
}
