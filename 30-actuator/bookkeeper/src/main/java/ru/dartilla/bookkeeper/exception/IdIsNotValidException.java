package ru.dartilla.bookkeeper.exception;

public class IdIsNotValidException extends BookkeeperException {

    public IdIsNotValidException() {
        super("Не удалось распознать идентификатор");
    }
}
