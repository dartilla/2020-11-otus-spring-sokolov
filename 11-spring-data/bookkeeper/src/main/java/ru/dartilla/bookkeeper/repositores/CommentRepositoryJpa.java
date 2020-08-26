package ru.dartilla.bookkeeper.repositores;

import org.springframework.stereotype.Repository;
import ru.dartilla.bookkeeper.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public void save(Comment comment) {
        em.persist(comment);
    }

}
