package ru.dartilla.examinator.util;

import ru.dartilla.examinator.domain.Answer;
import ru.dartilla.examinator.domain.Question;

public final class QuestionPrinter {

    public static String printQuestion(Question question) {
        StringBuilder sb = new StringBuilder("\t");
        for (Answer answer : question.getAnswersToChoose()) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(answer.getContent());
        }
        sb.append(System.lineSeparator());
        sb.insert(0, question.getContent() + System.lineSeparator());

        String message = sb.toString();
        System.out.println(message);
        return message;
    }
}
