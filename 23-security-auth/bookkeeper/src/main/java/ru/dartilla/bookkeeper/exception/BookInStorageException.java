package ru.dartilla.bookkeeper.exception;

public class BookInStorageException extends BookkeeperException {

    public BookInStorageException() {
        super("Книга уже в хранилище");
    }
}
