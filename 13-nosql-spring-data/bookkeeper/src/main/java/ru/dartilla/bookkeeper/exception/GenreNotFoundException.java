package ru.dartilla.bookkeeper.exception;

public class GenreNotFoundException extends BookkeeperException {

    public GenreNotFoundException(String name) {
        super("Не найден жанр " + name);
    }
}
