package ru.dartilla.bookkeeper.mms.sql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.mms.sql.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment getByMongoId(String mongoId);
}
