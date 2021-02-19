package ru.dartilla.bookkeeper.service;

import java.io.PrintStream;
import java.util.Scanner;

public interface InOut {

    PrintStream getOut();
    Scanner getIn();
}
