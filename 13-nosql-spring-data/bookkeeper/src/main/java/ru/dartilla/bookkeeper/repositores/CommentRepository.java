package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.domain.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
