package ru.dartilla.bookkeeper.exception;

public class CommentIsNotFound extends BookkeeperException {

    public CommentIsNotFound() {
        super("Не найден комментарий");
    }
}
