package ru.dartilla.examinator.service;

import org.springframework.stereotype.Service;
import ru.dartilla.examinator.domain.Exercise;
import ru.dartilla.examinator.domain.User;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Service
public class UserInterface {

    private final InOut inOut;

    public UserInterface(InOut inOut) {
        this.inOut = inOut;
    }

    public void printQuestion(Exercise exercise) {
        StringBuilder sb = new StringBuilder("\t");
        for (String answer : exercise.getAnswersToChoose()) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(answer);
        }
        sb.append(System.lineSeparator());
        sb.insert(0, System.lineSeparator() + exercise.getQuestion() + System.lineSeparator());

        String message = sb.toString();
        inOut.getOut().print(message);
    }

    public Set<String> readAnswer() {
        return Stream.of(inOut.getIn().nextLine().split(", ")).collect(toSet());
    }

    public void printExamPassed(User user, int rightAnswers) {
        inOut.getOut().println(System.lineSeparator() + getUserName(user)
                + ", You have passed examination. Right answers=" + rightAnswers + ". Congratulations!");
    }

    public void printExamFailed(User user, int rightAnswers) {
        inOut.getOut().println(System.lineSeparator() + getUserName(user)
                + ", You have failed examination. Right answers=" + rightAnswers + ". Let's try again!");
    }

    private String getUserName(User user) {
        return "Dear " + user.getName() + " " + user.getSurname();
    }

    public void printStartExam() {
        inOut.getOut().println();
        inOut.getOut().println("Please write down answers. If there are more than one right answer," +
                " than write it separated by comma and space (Ex: firstAnswer, secondAnswer)");
    }
}
