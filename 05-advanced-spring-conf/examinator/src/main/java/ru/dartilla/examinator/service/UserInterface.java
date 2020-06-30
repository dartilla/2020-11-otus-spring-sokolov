package ru.dartilla.examinator.service;

import org.springframework.stereotype.Service;
import ru.dartilla.examinator.config.localization.DefLocaleMessageSource;
import ru.dartilla.examinator.domain.ExamDetails;
import ru.dartilla.examinator.domain.Exercise;
import ru.dartilla.examinator.domain.ExerciseResult;
import ru.dartilla.examinator.domain.User;

import java.io.PrintStream;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Service
public class UserInterface {

    private final InOut inOut;
    private final DefLocaleMessageSource messageSource;

    public UserInterface(InOut inOut, DefLocaleMessageSource messageSource) {
        this.inOut = inOut;
        this.messageSource = messageSource;
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
                + messageSource.getMessage("ui.passedExam1") + rightAnswers + messageSource.getMessage("ui.passedExam2"));
    }

    public void printExamFailed(User user, int rightAnswers) {
        inOut.getOut().println(System.lineSeparator() + getUserName(user)
                + messageSource.getMessage("ui.failedExam1") + rightAnswers + messageSource.getMessage("ui.failedExam2"));
    }

    private String getUserName(User user) {
        return messageSource.getMessage("ui.greetingDear") + " " + user.getName() + " " + user.getSurname();
    }

    public void printStartExam() {
        PrintStream out = inOut.getOut();
        out.println();
        out.println(messageSource.getMessage("ui.pleaseWriteDownAnswers"));
    }

    public void printExamDetails(ExamDetails examDetails) {
        PrintStream out = inOut.getOut();
        out.println(messageSource.getMessage("ui.examDetails") + ":");
        out.println();
        int rightAnswers = 0;
        for (ExerciseResult result : examDetails.getExerciseResults()) {
            out.println(messageSource.getMessage("ui.examDetails.question") + ": " + result.getExercise().getQuestion());
            out.println(messageSource.getMessage("ui.examDetails.isAnsweredCorrect") + ": " + result.isAnsweredCorrect());
            out.println("----");
            if (result.isAnsweredCorrect()) {
                rightAnswers++;
            }
        }
        int rightAnswersToPassExam = examDetails.getRightAnswersToPassExam();
        out.println();
        out.println(messageSource.getMessage("ui.examDetails.totalQuestion") + ": " + examDetails.getExerciseResults().size());
        out.println(messageSource.getMessage("ui.examDetails.rightAnswers") + ": " + rightAnswers);
        out.println(messageSource.getMessage("ui.examDetails.rightAnswersToPassExam") + ": " + rightAnswersToPassExam);
    }
}
