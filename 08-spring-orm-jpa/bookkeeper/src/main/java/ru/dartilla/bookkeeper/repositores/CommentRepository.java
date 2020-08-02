package ru.dartilla.bookkeeper.repositores;

import ru.dartilla.bookkeeper.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Optional<Comment> findById(Long id);

    void save(Comment comment);

    List<Comment> findByScript(Long bookId);
}
