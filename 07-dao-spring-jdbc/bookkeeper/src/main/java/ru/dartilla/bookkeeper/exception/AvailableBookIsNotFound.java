package ru.dartilla.bookkeeper.exception;

public class AvailableBookIsNotFound extends BookkeeperException {

    public AvailableBookIsNotFound() {
        super("Не найдена свободная книга по запросу");
    }
}
