package ru.dartilla.bookkeeper.exception;

public class ScriptIsNotFound extends BookkeeperException {

    public ScriptIsNotFound() {
        super("Не найдена книга");
    }
}
