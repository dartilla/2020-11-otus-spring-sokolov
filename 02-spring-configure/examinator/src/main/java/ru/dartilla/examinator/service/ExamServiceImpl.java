package ru.dartilla.examinator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dartilla.examinator.domain.Excercise;
import ru.dartilla.examinator.domain.User;


@Service
public class ExamServiceImpl implements ExamService {

    private final Integer questionCountInExam;
    private final Integer rightAnswersToPassExam;
    private final ExerciseProvider exerciseProvider;
    private final AuthService authService;
    private final UserInterface userInterface;

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
        User user = authService.authenticate();
        userInterface.printStartExam();
        int currItemNumber = 0;
        int rightAnswers = 0;
        while (exerciseProvider.hasNext() && currItemNumber < questionCountInExam) {
            Excercise excercise = exerciseProvider.next();
            userInterface.printQuestion(excercise);
            if (userInterface.readAnswer().containsAll(excercise.getRightAnswers())) {
                rightAnswers++;
            }
            currItemNumber++;
        }
        exerciseProvider.close();
        if (rightAnswers < rightAnswersToPassExam) {
            userInterface.printExamFailed(user, rightAnswers);
        } else {
            userInterface.printExamPassed(user, rightAnswers);
        }
    }
}
