package ru.dartilla.examinator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@ToString
public class Question {
    private String content;
    private Set<Answer> rightAnswers;
    private List<Answer> answersToChoose;
}
