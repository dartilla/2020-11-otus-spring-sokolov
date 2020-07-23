package ru.dartilla.bookkeeper.exception;

public class BookIdIsNotValidException extends BookkeeperException {

    public BookIdIsNotValidException() {
        super("Не удалось распознать идентификатор книги");
    }
}
