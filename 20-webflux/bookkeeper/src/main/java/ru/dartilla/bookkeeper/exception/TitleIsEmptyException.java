package ru.dartilla.bookkeeper.exception;

public class TitleIsEmptyException extends BookkeeperException {

    public TitleIsEmptyException() {
        super("Заголовок не задан");
    }
}
