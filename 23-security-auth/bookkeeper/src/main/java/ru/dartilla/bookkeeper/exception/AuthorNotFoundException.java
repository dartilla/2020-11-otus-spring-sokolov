package ru.dartilla.bookkeeper.exception;

public class AuthorNotFoundException extends BookkeeperException {

    public AuthorNotFoundException() {
        super("Автор не найден");
    }
}
