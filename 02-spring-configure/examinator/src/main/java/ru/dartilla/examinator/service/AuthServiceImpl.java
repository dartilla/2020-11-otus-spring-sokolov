package ru.dartilla.examinator.service;

import org.springframework.stereotype.Service;
import ru.dartilla.examinator.domain.User;

import java.io.PrintStream;
import java.util.Scanner;

@Service
public class AuthServiceImpl implements AuthService {

    private final InOut inOut;

    public AuthServiceImpl(InOut inOut) {
        this.inOut = inOut;
    }

    @Override
    public User authenticate() {
        Scanner in = inOut.getIn();
        PrintStream out = inOut.getOut();
        out.println("Please enter your name..");
        String name = in.nextLine();
        out.println("Please enter your surname..");
        String surname = in.nextLine();
        return new User(name, surname);
    }
}
