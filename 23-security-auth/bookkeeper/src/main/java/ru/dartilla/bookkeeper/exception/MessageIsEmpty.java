package ru.dartilla.bookkeeper.exception;

public class MessageIsEmpty extends BookkeeperException {

    public MessageIsEmpty() {
        super("Пустое сообщение");
    }
}
