package ru.dartilla.examinator.service;

import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.Scanner;

@Service
public class InOutConsole implements InOut {

    @Override
    public PrintStream getOut() {
        return System.out;
    }

    @Override
    public Scanner getIn() {
        return new Scanner(System.in);
    }
}
