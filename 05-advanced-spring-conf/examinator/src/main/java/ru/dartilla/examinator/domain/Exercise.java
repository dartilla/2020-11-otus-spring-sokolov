package ru.dartilla.examinator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@ToString
public class Exercise {
    private String question;
    private Set<String> rightAnswers;
    private List<String> answersToChoose;
}
