package ru.dartilla.examinator.service;

import org.springframework.stereotype.Service;
import ru.dartilla.examinator.config.localization.DefLocaleMessageSource;
import ru.dartilla.examinator.domain.User;

import java.io.PrintStream;
import java.util.Scanner;

@Service
public class AuthServiceImpl implements AuthService {

    private final InOut inOut;
    private final DefLocaleMessageSource messageSource;

    public AuthServiceImpl(InOut inOut, DefLocaleMessageSource messageSource) {
        this.inOut = inOut;
        this.messageSource = messageSource;
    }

    @Override
    public User authenticate() {
        Scanner in = inOut.getIn();
        PrintStream out = inOut.getOut();
        out.println(messageSource.getMessage("auth.enterName"));
        String name = in.nextLine();
        out.println(messageSource.getMessage("auth.enterSurname"));
        String surname = in.nextLine();
        return new User(name, surname);
    }
}
