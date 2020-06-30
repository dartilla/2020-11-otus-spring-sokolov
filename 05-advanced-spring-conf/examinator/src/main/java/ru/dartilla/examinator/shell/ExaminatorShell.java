package ru.dartilla.examinator.shell;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.dartilla.examinator.config.localization.DefLocaleMessageSource;
import ru.dartilla.examinator.domain.User;
import ru.dartilla.examinator.service.AuthService;
import ru.dartilla.examinator.service.ExamService;

@ShellComponent
public class ExaminatorShell {

    private final ExamService examService;
    private final DefLocaleMessageSource messageSource;

    private boolean isExamFinished = false;

    public ExaminatorShell(ExamService examService, DefLocaleMessageSource messageSource, AuthService authService) {
        this.examService = examService;
        this.messageSource = messageSource;
    }

    @ShellMethod(value = "Start examination process", key = {"examStart"})
    public void startNewExam(@ShellOption(value = "-n", defaultValue = "") String name,
                             @ShellOption(value = "-s", defaultValue = "") String surname) {
        if (StringUtils.isNotBlank(name) || StringUtils.isNotBlank(surname)) {
            examService.doExam(new User(name, surname));
        } else {
            examService.doExam();
        }
        isExamFinished = true;
    }

    @ShellMethod(value = "Last examination details", key = {"examDetail"})
    @ShellMethodAvailability("isLastExamDetailsAvaliable")
    public void lastExamDetails() {
        examService.printDetail();
    }

    private Availability isLastExamDetailsAvaliable() {
        return isExamFinished ? Availability.available() : Availability.unavailable(
                messageSource.getMessage("shell.examNotFinishedYet"));
    }
}
