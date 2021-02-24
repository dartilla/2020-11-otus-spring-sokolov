package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.domain.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByScriptId(Long scriptId);
}
