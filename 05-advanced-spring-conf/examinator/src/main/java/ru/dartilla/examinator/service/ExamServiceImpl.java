package ru.dartilla.examinator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dartilla.examinator.domain.ExamDetails;
import ru.dartilla.examinator.domain.Exercise;
import ru.dartilla.examinator.domain.ExerciseResult;
import ru.dartilla.examinator.domain.User;

import java.util.ArrayList;
import java.util.List;


@Service
public class ExamServiceImpl implements ExamService {

    private final Integer questionCountInExam;
    private final Integer rightAnswersToPassExam;
    private final ExerciseProvider exerciseProvider;
    private final AuthService authService;
    private final UserInterface userInterface;

    private ExamDetails lastExamDetails;

    public ExamServiceImpl(@Value("${questionCountInExam}") int questionCountInExam,
                           @Value("${rightAnswersToPassExam}") int rightAnswersToPassExam,
                           ExerciseProvider exerciseProvider,
                           AuthService authService,
                           UserInterface userInterface) {
        this.exerciseProvider = exerciseProvider;
        this.questionCountInExam = questionCountInExam;
        this.rightAnswersToPassExam = rightAnswersToPassExam;
        this.authService = authService;
        this.userInterface = userInterface;
        if (this.questionCountInExam < 1) {
            throw new RuntimeException("questionCountInTest must be positive, but=" + this.questionCountInExam);
        }
        if (this.rightAnswersToPassExam < 1) {
            throw new RuntimeException("rightAnswersToPassExam must be positive, but=" + this.rightAnswersToPassExam);
        }
    }

    @Override
    public void doExam() {
        doExam(authService.authenticate());
    }

    @Override
    public void doExam(User user) {
        userInterface.printStartExam();
        List<ExerciseResult> exerciseResults = new ArrayList<>();
        int currItemNumber = 0;
        int rightAnswers = 0;
        exerciseProvider.refresh();
        while (exerciseProvider.hasNext() && currItemNumber < questionCountInExam) {
            Exercise exercise = exerciseProvider.next();
            userInterface.printQuestion(exercise);
            if (userInterface.readAnswer().containsAll(exercise.getRightAnswers())) {
                rightAnswers++;
                exerciseResults.add(new ExerciseResult(exercise, true));
            } else {
                exerciseResults.add(new ExerciseResult(exercise, false));
            }
            currItemNumber++;
        }
        exerciseProvider.close();
        boolean isExamPassed = rightAnswers < rightAnswersToPassExam;
        if (isExamPassed) {
            userInterface.printExamFailed(user, rightAnswers);
        } else {
            userInterface.printExamPassed(user, rightAnswers);
        }
        lastExamDetails = new ExamDetails(exerciseResults, rightAnswersToPassExam, user);
    }

    @Override
    public void printDetail() {
        if (lastExamDetails != null) {
            userInterface.printExamDetails(lastExamDetails);
        }
    }
}
