package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.dartilla.bookkeeper.domain.Comment;

import java.util.List;


public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByScriptId(String scriptId);
}
