package ru.dartilla.bookkeeper.repositores;

import org.springframework.stereotype.Repository;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.repositores.exception.MergeEntityNotExistingId;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class ScriptRepositoryJpa implements ScriptRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Script save(Script script) {
        if (script.getId() == null) {
            em.persist(script);
            return script;
        } else if (em.find(Script.class, script.getId()) != null) {
            return em.merge(script);
        } else {
            throw new MergeEntityNotExistingId();
        }
    }

    @Override
    public Script getById(Long id) {
        return em.find(Script.class, id);
    }

    @Override
    public Optional<Script> findById(Long id) {
        return Optional.ofNullable(getById(id));
    }

    @Override
    public List<Script> getAll() {
        return em.createQuery("select s from Script s join fetch s.author order by s.author.name, s.title", Script.class)
                .getResultList();
    }

    @Override
    public Optional<Script> findByAuthorIdAndTitle(Long authorId, String title) {
        TypedQuery<Script> query = em.createQuery("select s from Script s where s.author.id = :authorId" +
                " and s.title = :title", Script.class);
        query.setParameter("authorId", authorId);
        query.setParameter("title", title);
        return query.getResultList().stream().findAny();
    }
}
