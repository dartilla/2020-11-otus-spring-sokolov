package ru.dartilla.bookkeeper.exception;

public class BookNotFoundException extends BookkeeperException {

    public BookNotFoundException() {
        super("Не найдена экземпляр книги");
    }
}
