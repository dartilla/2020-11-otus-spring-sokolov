package ru.dartilla.bookkeeper.exception;

public class ScriptIsNotFoundException extends BookkeeperException {

    public ScriptIsNotFoundException() {
        super("Не найдена книга");
    }
}
