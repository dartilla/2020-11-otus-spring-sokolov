package ru.dartilla.bookkeeper.repositores;

import ru.dartilla.bookkeeper.domain.Comment;

import java.util.Optional;

public interface CommentRepository {

    Optional<Comment> findById(Long id);

    void save(Comment comment);
}
