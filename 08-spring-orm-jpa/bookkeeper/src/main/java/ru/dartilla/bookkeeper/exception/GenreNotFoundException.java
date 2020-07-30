package ru.dartilla.bookkeeper.exception;

public class GenreNotFoundException extends BookkeeperException {

    public GenreNotFoundException() {
        super("Не найден жанр по запросу");
    }
}
